import model.User
import repository.UserDaoRepository

class FindUserUseCaseImpl(
    private val userDaoRepository: UserDaoRepository
) : FindUserUseCase {
    override fun execute(id: Int): User {
        return userDaoRepository.find(id)
    }
}
