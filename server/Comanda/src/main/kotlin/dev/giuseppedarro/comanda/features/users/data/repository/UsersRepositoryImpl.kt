package dev.giuseppedarro.comanda.features.users.data.repository

import dev.giuseppedarro.comanda.features.users.data.Users
import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UsersRepositoryImpl : UsersRepository {
    override suspend fun createUser(
        employeeId: String,
        name: String,
        passwordHash: String,
        role: String
    ): Result<User> {
        return try {
            val insertedUser = transaction {
                // Check if user already exists
                if (Users.select { Users.employeeId eq employeeId }.count() > 0) {
                    throw IllegalArgumentException("User with employeeId $employeeId already exists")
                }

                val id = Users.insert {
                    it[Users.employeeId] = employeeId
                    it[Users.name] = name
                    it[Users.password] = passwordHash
                    it[Users.role] = role
                } get Users.id

                User(
                    id = id,
                    employeeId = employeeId,
                    name = name,
                    role = role
                )
            }
            Result.success(insertedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmployeeId(employeeId: String): User? {
        return transaction {
            Users.select { Users.employeeId eq employeeId }
                .map {
                    User(
                        id = it[Users.id],
                        employeeId = it[Users.employeeId],
                        name = it[Users.name],
                        role = it[Users.role]
                    )
                }
                .singleOrNull()
        }
    }
}
