package database.repository

import database.dao.UserDao
import database.table.Users
import model.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import repository.UserDaoRepository

class UserDaoRepositoryImpl : UserDaoRepository {
    private val passwordEncoder = BCryptPasswordEncoder()

    override fun find(id: Int): User {
        return transaction {
            return@transaction UserDao.findById(id)?.let {
                User(
                    id = it.id.value,
                    name = it.name,
                    email = it.email,
                    encryptedPassword = it.encryptedPassword,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            } ?: throw NotImplementedError()
        }
    }

    override fun findByEmailAndPassword(email: String, password: String): User {
        return transaction {
            val user = UserDao.find { Users.email.eq(email) }.first()
            if (passwordEncoder.matches(password, user.encryptedPassword)) {
                return@transaction User(
                    id = user.id.value,
                    name = user.name,
                    email = user.email,
                    encryptedPassword = user.encryptedPassword,
                    createdAt = user.createdAt,
                    updatedAt = user.updatedAt
                )
            } else {
                throw NotImplementedError()
            }
        }
    }

    override fun create(name: String, email: String, password: String) {
        transaction {
            UserDao.new {
                this.name = name
                this.email = email
                this.encryptedPassword = passwordEncoder.encode(password)
                this.createdAt = DateTime.now()
                this.updatedAt = DateTime.now()
            }
        }
    }
}