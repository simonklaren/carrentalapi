package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dto.SigningInUserDto
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

// Functie om JWT-authenticatie (JSON Web Token) in te stellen voor de applicatie
fun Application.configureAuthentication() {

    // Haal de JWT-secret op uit de configuratie; als deze niet is gevonden, gebruik dan "Not Found" als default waarde.
    // Het JWT-secret wordt gebruikt om tokens te versleutelen en te verifiëren.
    val jwtSecret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "Not Found"
    environment.log.info("Loaded JWT Secret: $jwtSecret")

    // Verzamelt belangrijke JWT-configuratie-eigenschappen zoals secret, issuer (uitgever), audience (doelgroep) en realm (domein).
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    // Instellen van JWT-authenticatie in Ktor
    authentication {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret)) // Gebruik het HMAC256-algoritme om tokens te ondertekenen
                    .withAudience(audience) // Verwacht deze audience in het token
                    .withIssuer(issuer) // Verwacht deze issuer in het token
                    .build()
            )
            validate { credential ->
                // Valideer of het token de juiste audience bevat; anders wordt het geweigerd
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload) else null
            }
        }
    }

    routing {
        // Initialiseer een instantie van `UserService`, die verantwoordelijk is voor gebruikersbeheer zoals authenticatie.
        val userService = UserService()

        // Route voor inloggen; hier kan de gebruiker inloggen en, als succesvol, een JWT-token ontvangen.
        post("/login") {
            // Ontvangt gebruikersgegevens (zoals email en wachtwoord) van de request body
            val user = call.receive<SigningInUserDto>()

            // Controleer of de gebruiker kan worden geauthenticeerd met de `authenticateUser` functie in `UserService`
            val isAuthenticated = userService.authenticateUser(user.email, user.password)

            if (isAuthenticated) {
                // Maak een JWT-token aan als de gebruiker succesvol is geauthenticeerd
                val token = JWT.create()
                    .withAudience(audience) // Voeg de audience claim toe aan het token
                    .withIssuer(issuer) // Voeg de issuer claim toe aan het token
                    .withClaim("email", user.email) // Voeg de email van de gebruiker toe als een claim
                    .withClaim("password", user.password) // Voeg het wachtwoord toe als een claim (alleen in een beveiligde omgeving)
                    .withClaim("realm", "access to upload") // Geef het domein van rechten aan
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000)) // Stel de vervaltijd in op 60 seconden vanaf nu
                    .sign(Algorithm.HMAC256(secret)) // Onderteken het token met het secret
                call.respond(token) // Stuur het gegenereerde token als response terug naar de gebruiker
            } else {
                // Stuur een Unauthorized-statuscode terug als de inloggegevens onjuist zijn
                call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
            }
        }

        // Beveiligde route voor toegang via een geldig JWT-token
        authenticate("auth-jwt") {
            get("/me") {
                // Haal de JWT-principal (geauthenticeerde gebruiker) op uit de aanvraag
                val principal = call.principal<JWTPrincipal>()

                // `!!` wordt hier gebruikt om aan te geven dat `principal` niet null mag zijn; als het wel null is, wordt een fout veroorzaakt.
                // `?.` wordt gebruikt bij `expiresAt` om null-veilig de vervaltijd van het token op te halen, mocht deze null zijn.
                val username = principal!!.payload.getClaim("email").asString() // Verkrijg de email uit het token
                val realm = principal.payload.getClaim("realm").asString() // Verkrijg de rechten van de gebruiker
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis()) // Bereken de resterende tijd tot het token verloopt

                // Stuur een bericht terug naar de gebruiker met hun email, token-vervaltijd en toegangsrechten
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. You have $realm")
            }
        }
    }
}
