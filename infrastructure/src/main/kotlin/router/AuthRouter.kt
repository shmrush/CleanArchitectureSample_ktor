package router

import controller.UsersController
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.koin.ktor.ext.inject
import respondCustomResponse

@KtorExperimentalLocationsAPI
class AuthRouterLocations {
    @Location("/api/auth/sign_in")
    data class SignIn(var email: String = "", var password: String = "")

    @Location("/api/auth/sign_up")
    data class SignUp(
        var name: String = "",
        var email: String = "",
        var password: String = "",
        var passwordConfirmation: String = ""
    )
}

@KtorExperimentalLocationsAPI
fun Route.authRouter() {
    val usersController by inject<UsersController>()

    post<AuthRouterLocations.SignUp> {
        val params = call.receive<AuthRouterLocations.SignUp>()
        val response = usersController.signUp(
            UsersController.SignUpParams(
                params.name,
                params.email,
                params.password,
                params.passwordConfirmation
            )
        )
        call.respondCustomResponse(response)
    }

    post<AuthRouterLocations.SignIn> {
        val params = call.receive<AuthRouterLocations.SignIn>()
        val response = usersController.signIn(
            UsersController.SignInParams(
                params.email,
                params.password
            )
        )
        call.sessions.set(response.data)
        call.respondCustomResponse(response, true)
    }
}
