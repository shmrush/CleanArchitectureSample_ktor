interface SignUpUseCase {
    fun execute(name: String, email: String, password: String, passwordConfirmation: String)
}
