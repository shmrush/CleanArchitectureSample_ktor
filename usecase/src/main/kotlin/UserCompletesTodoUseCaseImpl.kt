import repository.TodoDaoRepository
import repository.UserDaoRepository

class UserCompletesTodoUseCaseImpl(
    private val userDaoRepository: UserDaoRepository,
    private val todoDaoRepository: TodoDaoRepository
) : UserCompletesTodoUseCase {
    override fun execute(userId: Int, id: Int) {
        val user = userDaoRepository.find(userId)
        todoDaoRepository.updateCompleted(user, id, true)
    }
}
