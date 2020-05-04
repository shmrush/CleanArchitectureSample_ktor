package csrf

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.CookieEncoding
import io.ktor.http.HttpMethod
import io.ktor.request.ApplicationRequest
import io.ktor.request.header
import io.ktor.request.httpMethod
import io.ktor.request.uri
import io.ktor.sessions.CurrentSession
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.AttributeKey
import org.apache.commons.lang3.RandomStringUtils
import java.util.*
import kotlin.experimental.xor

class CSRFProtection(config: Configuration) {
    private val CSRF_TOKEN_LENGTH = 64
    private val headerName = config.headerName
    private val cookieName = config.cookieName
    private lateinit var request: ApplicationRequest
    private lateinit var session: CurrentSession

    class Configuration {
        var headerName = "X-CSRF-Token"
        var cookieName = "_csrf_token"
    }

    private fun intercept(call: ApplicationCall) {
        this.request = call.request
        this.session = call.sessions
        if (!isVerifiedRequest()) {
            throw NotImplementedError("Invalid CSRF Token")
        } else {
            call.response.cookies.append(cookieName, maskedCSRFToken(), CookieEncoding.RAW)
        }
    }

    private fun isVerifiedRequest(): Boolean {
        val method = request.httpMethod
        return method == HttpMethod.Get
                || method == HttpMethod.Head
                || (isCSRFTokenValid() && isRequestOriginValid())
    }

    private fun isRequestOriginValid(): Boolean {
        return request.origin.uri.isBlank() || request.origin.uri == request.uri
    }

    private fun isCSRFTokenValid(): Boolean {
        val encodedMaskedToken = request.header(headerName)
        if (encodedMaskedToken.isNullOrBlank()) return false

        val maskedToken = Base64.getDecoder().decode(encodedMaskedToken)
        return if (maskedToken.size == CSRF_TOKEN_LENGTH * 2) {
            unmaskToken(maskedToken).contentEquals(realCSRFToken().toByteArray())
        } else {
            false
        }
    }

    private fun realCSRFToken(): String {
        val token = session.get<CSRFSession>()?.realCSRFToken
        return token ?: RandomStringUtils.randomAlphanumeric(CSRF_TOKEN_LENGTH).apply {
            val csrfSession = CSRFSession(this)
            session.set(csrfSession)
        }
    }

    private fun maskedCSRFToken(): String {
        val oneTimePad = RandomStringUtils.randomAlphanumeric(CSRF_TOKEN_LENGTH).toByteArray()
        val encryptedCSRFToken = oneTimePad.mapIndexed { index, pad -> pad xor realCSRFToken().toByteArray()[index] }
        val maskedToken = oneTimePad + encryptedCSRFToken
        return Base64.getEncoder().encodeToString(maskedToken)
    }

    private fun unmaskToken(maskedToken: ByteArray): ByteArray {
        val oneTimePad = maskedToken.slice(0 until  CSRF_TOKEN_LENGTH)
        val encryptedCSRFToken = maskedToken.slice(CSRF_TOKEN_LENGTH until maskedToken.size)
        return oneTimePad.mapIndexed { index, pad -> pad xor encryptedCSRFToken[index] }.toByteArray()
    }

    companion object Feature : ApplicationFeature<Application, Configuration, CSRFProtection> {
        override val key = AttributeKey<CSRFProtection>("CSRFProtection")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): CSRFProtection {
            val config = Configuration().apply(configure)
            val feature = CSRFProtection(config)
            pipeline.intercept(ApplicationCallPipeline.Features) { feature.intercept(call) }
            return feature
        }
    }
}

data class CSRFSession(val realCSRFToken: String?)
