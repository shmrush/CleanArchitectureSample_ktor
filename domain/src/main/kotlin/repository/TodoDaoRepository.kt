package repository

import model.Todo
import model.User
import org.joda.time.DateTime

interface TodoDaoRepository {
    fun find(id: Int): Todo
    fun getAll(user: User): List<Todo>
    fun create(user: User, title: String, description: String, deadlineAt: DateTime)
    fun updateCompleted(user: User, id: Int, completed: Boolean)
}