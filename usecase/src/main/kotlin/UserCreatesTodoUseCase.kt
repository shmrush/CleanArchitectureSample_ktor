import org.joda.time.DateTime

interface UserCreatesTodoUseCase {
    fun execute(userId: Int, title: String, description: String, deadlineAt: DateTime)
}
