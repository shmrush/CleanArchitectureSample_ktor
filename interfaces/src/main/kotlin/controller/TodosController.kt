package controller

import UserCompletesTodoUseCase
import UserCreatesTodoUseCase
import UserGetsAllTodosUseCase
import model.Session
import model.Todo
import org.joda.time.DateTime

class TodosController(
    private val userGetsAllTodosUseCase: UserGetsAllTodosUseCase,
    private val userCreatesTodoUseCase: UserCreatesTodoUseCase,
    private val userCompletesTodoUseCase: UserCompletesTodoUseCase
) {
    // GET /api/todos
    fun index(session: Session?): Response<List<Todo>> {
        return try {
            if (session == null) throw NoSuchElementException()
            val data = userGetsAllTodosUseCase.execute(session.userId)
            Response(200, data)
        } catch (error: Exception) {
            if (error is NoSuchElementException) {
                Response(404, errorMessage = error.message)
            } else {
                Response(500, errorMessage = error.message)
            }
        }
    }

    // POST /api/todos
    data class CreateParams(val title: String, val description: String, val deadlineAt: DateTime)

    fun create(session: Session?, params: CreateParams): Response<Unit> {
        return try {
            if (session == null) throw NoSuchElementException()
            userCreatesTodoUseCase.execute(session.userId, params.title, params.description, params.deadlineAt)
            Response(201)
        } catch (error: Exception) {
            Response(400, errorMessage = error.message)
        }
    }

    // PUT /api/todos/:id/complete
    fun complete(session: Session?, id: Int): Response<Unit> {
        return try {
            if (session == null) throw NoSuchElementException()
            userCompletesTodoUseCase.execute(session.userId, id)
            Response(201)
        } catch (error: Exception) {
            Response(400, errorMessage = error.message)
        }
    }
}