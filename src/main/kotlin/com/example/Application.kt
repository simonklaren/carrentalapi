package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction



fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Configureer de database
    Database.connect(
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver"
    )
    // Maak tables aan wanneer server wordt gestart
    transaction {
        SchemaUtils.create(VehicleTable)
    }


    configureSerialization()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureRouting()
}
