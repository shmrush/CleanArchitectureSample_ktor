package database.dao

import database.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(Users)

    var name by Users.name
    var email by Users.email
    var encryptedPassword by Users.encryptedPassword
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt
}

