import model.User
import repository.UserDaoRepository

class SignInUseCaseImpl(
    private val userDaoRepository: UserDaoRepository
): SignInUseCase {
    override fun execute(email: String, password: String): User {
        return userDaoRepository.findByEmailAndPassword(email, password)
    }
}
