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

fun Application.configureAuthentication() {

    val jwtSecret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "Not Found"
    environment.log.info("Loaded JWT Secret: $jwtSecret")

    // Please read the jwt property from the config file if you are using EngineMain
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    authentication {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload) else null
            }
        }
    }


    routing {
        val userService = UserService()

        post("/login") {
            // check if user exists
            val user = call.receive<SigningInUserDto>()

            // Authenticate user
            val isAuthentzicated = userService.authenticateUser(user.email, user.password)

            if (isAuthenticated) {
                // Als authenticatie succesvol is stuur jwt token
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("email", user.email)
                    .withClaim("password", user.password)
                    .withClaim("realm", "access to upload")
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(token)

            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid email or password")
            }

        }

        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("email").asString()
                val realm = principal!!.payload.getClaim("realm").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms. You have $realm")
            }
        }
    }
}



