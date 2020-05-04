import model.Todo
import repository.TodoDaoRepository
import repository.UserDaoRepository

class UserGetsAllTodosUseCaseImpl(
    private val userDaoRepository: UserDaoRepository,
    private val todoDaoRepository: TodoDaoRepository
) : UserGetsAllTodosUseCase {
    override fun execute(userId: Int): List<Todo> {
        val user = userDaoRepository.find(userId)
        return todoDaoRepository.getAll(user)
    }
}
