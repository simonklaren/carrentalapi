package com.example

import com.example.models.* // Importeer alle databasemodellen
import com.example.plugins.* // Importeer alle configuratiefuncties voor plugins
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

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
}
