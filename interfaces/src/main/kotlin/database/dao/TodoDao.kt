package database.dao

import database.table.Todos
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TodoDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TodoDao>(Todos)

    var user by UserDao referencedOn Todos.user
    var title by Todos.title
    var description by Todos.description
    var completed by Todos.completed
    var deadlineAt by Todos.deadlineAt
    var createdAt by Todos.createdAt
    var updatedAt by Todos.updatedAt
}
