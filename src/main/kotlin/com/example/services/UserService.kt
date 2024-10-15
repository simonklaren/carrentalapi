package com.example.services

import com.example.dto.UserRegistrationDto
import com.example.models.UserTable
import com.example.utils.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.serialization.Serializable


class UserService {

    // maakt user aan en hashed password
    fun addUser(user: UserRegistrationDto): Int = transaction {
        UserTable.insert { row ->
            row[email] = user.email
            row[password] = hashPassword(user.password)
        } get UserTable.id
    }


}