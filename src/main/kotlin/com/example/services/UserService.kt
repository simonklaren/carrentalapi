package com.example.services

import com.example.dto.UserRegistrationDto
import com.example.models.UserTable
import com.example.utils.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt


class UserService {

    // maakt user aan en hashed password
    fun addUser(user: UserRegistrationDto): Int? = transaction {
        // Check if user with the same email already exists
        val existingUser = UserTable.select { UserTable.email eq user.email }.singleOrNull()

        if (existingUser != null) {
            // User already exists, return null or handle the error
            null
        } else {
            // Insert new user
            UserTable.insert { row ->
                row[email] = user.email
                row[password] = hashPassword(user.password)
            } get UserTable.id
        }
    }

    // kijk of een user bestaat
    fun authenticateUser(email: String, password: String): Boolean = transaction {
        val user = UserTable.select { UserTable.email eq email }.singleOrNull()
        if (user != null) {
            // Check if the provided password matches the hashed password in the database
            BCrypt.checkpw(password, user[UserTable.password])
        } else {
            false
        }
    }




}