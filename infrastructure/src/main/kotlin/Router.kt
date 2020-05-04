
import controller.Response
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import router.authRouter
import router.todoRouter

@KtorExperimentalLocationsAPI
fun Route.router() {
    get("/") {
//        val session = call.sessions.get<Session>() ?: Session()
//        call.sessions.set(session)
//        call.response.csrfToken
        call.respond("root")
    }
    authRouter()
    todoRouter()
}

suspend fun <T> ApplicationCall.respondCustomResponse(response: Response<T>, onlyStatusCode: Boolean = false) {
    val statusCode = HttpStatusCodeConverter(response.statusCode).call()
    val errorMessage = response.errorMessage
    val data = response.data
    return if (errorMessage != null) {
        respond(statusCode, errorMessage)
    } else {
        if (data == null || onlyStatusCode) {
            respond(statusCode)
        } else {
            respond(statusCode, data)
        }
    }
}

data class HttpStatusCodeConverter(private val statusCode: Int) {
    fun call(): HttpStatusCode {
        return HttpStatusCode.allStatusCodes.find { it.value == statusCode }
            ?: throw NotImplementedError("No such statusCode exists.")
    }
}
