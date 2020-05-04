import model.User

interface FindUserUseCase {
    fun execute(id: Int): User
}
