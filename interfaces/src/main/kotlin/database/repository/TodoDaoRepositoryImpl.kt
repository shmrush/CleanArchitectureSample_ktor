package database.repository

import database.dao.TodoDao
import database.dao.UserDao
import database.table.Todos
import model.Todo
import model.User
import repository.TodoDaoRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class TodoDaoRepositoryImpl : TodoDaoRepository {
    override fun find(id: Int): Todo {
        return transaction {
            return@transaction TodoDao.findById(id)?.let {
                Todo(
                    id = it.id.value,
                    title = it.title,
                    description = it.description,
                    deadLineAt = it.deadlineAt,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            } ?: throw NoSuchElementException()
        }
    }

    override fun getAll(user: User): List<Todo> {
        return transaction {
            val todos = TodoDao.find { Todos.user eq user.id }.toList()
            return@transaction todos.map {
                Todo(
                    id = it.id.value,
                    title = it.title,
                    description = it.description,
                    deadLineAt = it.deadlineAt,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }

    override fun create(user: User, title: String, description: String, deadlineAt: DateTime) {
        transaction {
            val userData = UserDao.findById(user.id) ?: throw NoSuchElementException()
            TodoDao.new {
                this.user = userData
                this.title = title
                this.description = description
                this.completed = false
                this.deadlineAt = deadlineAt
                this.createdAt = DateTime.now()
                this.updatedAt = DateTime.now()
            }
        }
    }

    override fun updateCompleted(user: User, id: Int, completed: Boolean) {
        transaction {
            val todo = TodoDao.findById(id) ?: throw NoSuchElementException()
            if (todo.user.id.value != user.id) throw NoSuchElementException()
            todo.completed = completed
        }
    }
}