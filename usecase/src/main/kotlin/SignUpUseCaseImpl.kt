import repository.UserDaoRepository

class SignUpUseCaseImpl(
    private val userDaoRepository: UserDaoRepository
) : SignUpUseCase {
    override fun execute(name: String, email: String, password: String, passwordConfirmation: String) {
        if (password != passwordConfirmation) throw NotImplementedError("Invalid password")
        userDaoRepository.create(
            name = name,
            email = email,
            password = password
        )
    }
}
