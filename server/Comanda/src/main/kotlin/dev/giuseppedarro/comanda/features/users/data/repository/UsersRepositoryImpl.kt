package dev.giuseppedarro.comanda.features.users.data.repository

import dev.giuseppedarro.comanda.features.users.data.Users
import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = transaction {
                Users.selectAll().map {
                    User(
                        id = it[Users.id],
                        employeeId = it[Users.employeeId],
                        name = it[Users.name],
                        role = it[Users.role]
                    )
                }
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        id: Int,
        employeeId: String?,
        name: String?,
        passwordHash: String?,
        role: String?
    ): Result<User> {
        return try {
            val updatedUser = transaction {
                // Check if user exists
                if (Users.select { Users.id eq id }.count() == 0L) {
                    throw IllegalArgumentException("User with id $id not found")
                }

                // Check if new employeeId is already taken by another user
                if (employeeId != null) {
                    val existing = Users.select { Users.employeeId eq employeeId }.singleOrNull()
                    if (existing != null && existing[Users.id] != id) {
                        throw IllegalArgumentException("User with employeeId $employeeId already exists")
                    }
                }

                Users.update({ Users.id eq id }) {
                    if (employeeId != null) it[Users.employeeId] = employeeId
                    if (name != null) it[Users.name] = name
                    if (passwordHash != null) it[Users.password] = passwordHash
                    if (role != null) it[Users.role] = role
                }

                Users.select { Users.id eq id }
                    .map {
                        User(
                            id = it[Users.id],
                            employeeId = it[Users.employeeId],
                            name = it[Users.name],
                            role = it[Users.role]
                        )
                    }
                    .single()
            }
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: Int): Result<Unit> {
        return try {
            transaction {
                val deletedCount = Users.deleteWhere { Users.id eq id }
                if (deletedCount == 0) {
                    throw IllegalArgumentException("User with id $id not found")
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
