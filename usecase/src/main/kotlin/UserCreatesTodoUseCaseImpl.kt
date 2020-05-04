import org.joda.time.DateTime
import repository.TodoDaoRepository
import repository.UserDaoRepository

class UserCreatesTodoUseCaseImpl(
    private val userDaoRepository: UserDaoRepository,
    private val todoDaoRepository: TodoDaoRepository
) : UserCreatesTodoUseCase {
    override fun execute(userId: Int, title: String, description: String, deadlineAt: DateTime) {
        val user = userDaoRepository.find(userId)
        todoDaoRepository.create(user, title, description, deadlineAt)
    }
}
