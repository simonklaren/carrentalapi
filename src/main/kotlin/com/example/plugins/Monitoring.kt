package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.*

// Functie om CallLogging in te stellen, wat het loggen van inkomende HTTP-verzoeken mogelijk maakt.
// Dit helpt bij het monitoren en debuggen van de applicatie door informatie over verzoeken vast te leggen.
fun Application.configureMonitoring() {
    install(CallLogging) {
        // Stel het logniveau in op INFO, zodat alleen informatieve berichten worden gelogd.
        level = Level.INFO

        // Filter om alleen verzoeken te loggen waarvan het pad begint met "/".
        // Dit kan nuttig zijn om alleen relevante verzoeken te loggen, bijvoorbeeld om statische bestanden over te slaan.
        filter { call -> call.request.path().startsWith("/") }
    }
}
