package database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object Users : IntIdTable("users") {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val encryptedPassword = varchar("encrypted_password", 255)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
