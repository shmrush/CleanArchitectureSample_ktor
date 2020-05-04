package controller

import SignInUseCase
import SignUpUseCase
import model.Session

class UsersController(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) {
    // POST /api/auth/sign_in
    data class SignInParams(val email: String, val password: String)

    fun signIn(params: SignInParams): Response<Session> {
        return try {
            val data = signInUseCase.execute(params.email, params.password)
            Response(200, Session(data.id))
        } catch (error: Exception) {
            Response(400, errorMessage = error.message)
        }
    }

    // POST /api/auth/sign_up
    data class SignUpParams(val name: String, val email: String, val password: String, val passwordConfirmation: String)

    fun signUp(params: SignUpParams): Response<Nothing> {
        return try {
            signUpUseCase.execute(params.name, params.email, params.password, params.passwordConfirmation)
            Response(201)
        } catch (error: Exception) {
            Response(400, errorMessage = error.message)
        }
    }
}