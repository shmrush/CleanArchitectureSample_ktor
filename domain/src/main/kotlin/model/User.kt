package model

import org.joda.time.DateTime

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val encryptedPassword: String,
    val createdAt: DateTime,
    val updatedAt: DateTime
)