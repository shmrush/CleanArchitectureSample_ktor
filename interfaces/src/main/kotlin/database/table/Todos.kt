package database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object Todos : IntIdTable("todos") {
    val user = reference("user_id", Users)
    val title = varchar("title", 255)
    val description = text("description")
    val completed = bool("completed")
    val deadlineAt = datetime("deadline_at")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}