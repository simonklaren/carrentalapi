package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Column

// Functie om JSON-serialisatie in te stellen in de applicatie
// Dit zorgt ervoor dat data automatisch wordt omgezet naar JSON-formaat bij het verzenden en ontvangen
fun Application.configureSerialization() {
    // Installeert ContentNegotiation met JSON-ondersteuning
    install(ContentNegotiation) {
        json() // Stelt JSON in als het standaardformaat voor serialisatie
    }

    // Definieert een route voor het testen van JSON-output
    routing {
        get("/json/kotlinx-serialization") {
            // Stuur een simpele JSON-response terug met een key-value pair
            call.respond(mapOf("hello" to "world"))
        }
    }
}

// Definieer een data class voor gebruikers met email en wachtwoord
// Met het @Serializable annotatie kan Kotlin deze data class omzetten naar JSON
@Serializable
data class User(
    val email: String, // Het e-mailadres van de gebruiker
    val password: String // Het wachtwoord van de gebruiker
)

// Definieer een data class voor voertuigen met eigenschappen zoals merk en model
@Serializable
data class Vehicle(
    val id: Int = 0, // ID met een standaardwaarde van 0; handig bij het toevoegen van nieuwe voertuigen
    val email: String,
    val brand: String, // Merk van het voertuig
    val model: String, // Model van het voertuig
    val type: String, // Type voertuig, bijvoorbeeld ICE (benzine), BEV (elektrisch), etc.
    val pricePerDay: Int, // De prijs per dag voor het huren van het voertuig
    val color: String,
    val imageURL: String = "",
    val rentDateFrom: String,
    val rentDateTo: String,
    val lat: Double,
    val long: Double,
    )
