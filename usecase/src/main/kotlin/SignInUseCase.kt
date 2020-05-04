import model.User

interface SignInUseCase {
    fun execute(email: String, password: String): User
}
