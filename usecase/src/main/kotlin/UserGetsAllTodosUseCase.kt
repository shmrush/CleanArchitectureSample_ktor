import model.Todo

interface UserGetsAllTodosUseCase {
    fun execute(userId: Int): List<Todo>
}
