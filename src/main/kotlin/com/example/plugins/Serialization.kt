package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
        }
    }
}

@Serializable
data class User(
    val email: String,
    val password: String
)

@Serializable
data class Vehicle(
    val id: Int = 0, // Zorg dat de ID een standaardwaarde heeft (anders kan deze error veroorzaken)
    val brand: String,
    val model: String,
    val type: String,
    val pricePerDay: Double
)
