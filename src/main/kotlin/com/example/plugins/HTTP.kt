package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

// Functie om CORS (Cross-Origin Resource Sharing) in te stellen voor de applicatie.
// Dit zorgt ervoor dat de applicatie kan communiceren met clients van andere domeinen.
fun Application.configureHTTP() {
    install(CORS) {
        // Sta bepaalde HTTP-methoden toe, zoals OPTIONS, PUT, DELETE en PATCH, die standaard mogelijk geblokkeerd worden door CORS.
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        // Sta bepaalde headers toe in verzoeken, zoals Authorization (voor JWT-tokens) en een aangepaste header.
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")

        // Sta verzoeken van elk domein toe. Dit is handig voor ontwikkeling, maar in productie is het veiliger om dit te beperken tot specifieke domeinen.
        anyHost() // @TODO: Vermijd dit in productie indien mogelijk. Beperk tot specifieke hosts.
    }
}
