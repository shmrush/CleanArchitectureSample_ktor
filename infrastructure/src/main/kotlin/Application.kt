import com.fasterxml.jackson.databind.SerializationFeature
import config.config
import config.database
import csrf.CSRFProtection
import csrf.CSRFSession
import di.interfacesModule
import di.useCaseModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.session
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.HSTS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.directorySessionStorage
import io.ktor.util.KtorExperimentalAPI
import model.Session
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import java.io.File
import kotlin.collections.set

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    Database.connect(
        config[database.url],
        config[database.driver],
        config[database.user],
        config[database.password]
    )
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(
            interfacesModule,
            useCaseModule
        )
    }
    install(Locations)
    install(Routing)
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(HSTS) {
        includeSubDomains = true
    }
    install(Sessions) {
        cookie<Session>("_ktor_auth_session", directorySessionStorage(File(".sessions/auth"))) {
            cookie.extensions["SameSite"] = "lax"
            cookie.httpOnly = true
            cookie.path = "/"
        }
        cookie<CSRFSession>("_ktor_csrf_session", directorySessionStorage(File(".sessions/csrf"))) {
            cookie.extensions["SameSite"] = "lax"
            cookie.httpOnly = true
            cookie.path = "/"
        }
    }
    install(Authentication) {
        session<Session>("session-auth") {
            val findUserUseCase by inject<FindUserUseCase>()

            data class AccountPrincipal(val id: Int) : Principal
            validate { session ->
                findUserUseCase.execute(session.userId).run {
                    AccountPrincipal(this.id)
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
    install(CSRFProtection) {}

    routing {
        router()
    }
}
