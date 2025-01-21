package com.example

import com.example.models.* // Importeer alle databasemodellen
import com.example.plugins.* // Importeer alle configuratiefuncties voor plugins
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*

// De `main` functie start de Ktor-server met behulp van de Netty-engine
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args) // Start de server met Netty als HTTP-server
}

// Hoofdconfiguratiefunctie voor de applicatie; wordt uitgevoerd wanneer de server start
fun Application.module() {

    // Log informatie over het configuratiebestand, nuttig voor debugging en inzicht in de instellingen
    environment.log.info("Config file: ${environment.config.toMap()}")

    // Configureer de databaseverbinding met H2, een in-memory database
    Database.connect(
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", // Database URL; DB_CLOSE_DELAY=-1 zorgt dat de database actief blijft tijdens de sessie
        driver = "org.h2.Driver" // Specificeer de H2 database driver
    )

    // Voer een transactie uit om de benodigde database-tabellen aan te maken bij het starten van de server
    transaction {
        SchemaUtils.create(VehicleTable) // Creëer de `VehicleTable` als deze nog niet bestaat
        SchemaUtils.create(UserTable) // Creëer de `UserTable` als deze nog niet bestaat
    }

    // Configureer de verschillende modules/plugins van Ktor
    configureAuthentication() // Zet authenticatie op (JWT)
    configureHTTP() // Stel HTTP-opties in, zoals CORS
    configureSerialization() // Configureer JSON-serialisatie voor data-uitwisseling
    configureMonitoring() // Stel logging en monitoring in om verzoeken en errors te volgen
    configureRouting() // Definieer de routes voor de API
    seedDatabase() // Seed database met test info
}



fun Application.seedDatabase() {
    environment.log.info("Seeding the database with default data...")

    runBlocking {
        transaction {
            // Check of de database al gevuld is
            if (VehicleTable.selectAll().empty()) {
                // Voeg standaard voertuigen toe
                listOf(
                    Vehicle(
                        id = 1,
                        brand = "Ford",
                        model = "Mustang",
                        type = "BEV",
                        pricePerDay = 95,
                        color = "Color(1.0, 0.0, 0.0, 1.0, sRGB IEC61966-2.1)",
                        imageURL = "uploads/upload1250559315408875231.jpg",
                        rentDateFrom = "03-03-2025",
                        rentDateTo = "14-03-2025"
                    ),
                    Vehicle(
                        id = 2,
                        brand = "Lamborgini",
                        model = "Urus",
                        type = "BEV",
                        pricePerDay = 165,
                        color = "Color(0.0, 0.0, 1.0, 1.0, sRGB IEC61966-2.1)",
                        imageURL = "uploads/upload3551636757534789668.jpg",
                        rentDateFrom = "03-02-2025",
                        rentDateTo = "21-02-2025"
                    ),
                    Vehicle(
                        id = 3,
                        brand = "Audi",
                        model = "Rs6",
                        type = "BEV",
                        pricePerDay = 195,
                        color = "Color(1.0, 1.0, 1.0, 1.0, sRGB IEC61966-2.1)",
                        imageURL = "uploads/upload2733683574846092242.jpg",
                        rentDateFrom = "27-01-2025",
                        rentDateTo = "24-02-2025"
                    )
                    // Voeg meer voertuigen toe als nodig
                ).forEach { vehicle ->
                    VehicleTable.insert {
                        it[brand] = vehicle.brand
                        it[model] = vehicle.model
                        it[type] = vehicle.type
                        it[pricePerDay] = vehicle.pricePerDay
                        it[color] = vehicle.color
                        it[imageURL] = vehicle.imageURL
                        it[rentDateFrom] = vehicle.rentDateFrom
                        it[rentDateTo] = vehicle.rentDateTo
                    }
                }
                environment.log.info("Database seeded with default vehicles.")
            } else {
                environment.log.info("Database already contains data, skipping seeding.")
            }
        }
    }
}

