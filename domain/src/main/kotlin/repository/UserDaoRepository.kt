package repository

import model.User

interface UserDaoRepository {
    fun find(id: Int): User
    fun findByEmailAndPassword(email: String, password: String): User
    fun create(name: String, email: String, password: String)
}