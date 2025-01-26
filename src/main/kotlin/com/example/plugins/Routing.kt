package com.example.plugins

import com.example.services.VehicleService
import com.example.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.ktor.http.*
import java.io.File

// Functie om het pad te verkrijgen waar afbeeldingen worden geüpload
// `imagefile` is de naam van het afbeeldingsbestand en wordt toegevoegd aan de "uploads" map.
private fun getImageUploadPath(imagefile: String) = "uploads/$imagefile"

// Functie om alle routes in de applicatie te configureren
fun Application.configureRouting() {
    // `routing` definieert een blok waarin alle paden en routes worden ingesteld voor de server
    routing {
        // Maak instanties van de services aan die de logica bevatten voor gebruikers- en voertuigenbeheer
        val vehicleService = VehicleService() // Beheert alle voertuigacties (CRUD)
        val userService = UserService() // Beheert alle gebruikersacties (bijv. aanmaken van een account)

        // POST-aanvraag: Registreer een nieuwe gebruiker
        post("/signup") {
            println("Incoming POST request...") // Logbericht voor debugging, toont dat er een verzoek binnenkomt

            // Probeer de body van de aanvraag te lezen en om te zetten naar een User object.
            val user = try {
                call.receive<User>() // Leest JSON-data en converteert deze naar een User object
            } catch (e: Exception) {
                // Als de data niet correct is (bijv. ontbrekende velden), stuur dan een foutmelding terug naar de client
                println("Failed to parse request body: ${e.message}") // Logt de foutmelding
                return@post call.respondText("Invalid data format", status = HttpStatusCode.BadRequest)
            }

            // Voeg de gebruiker toe aan de database via `userService`.
            // Als `result` niet `null` is, betekent dit dat de gebruiker succesvol is aangemaakt.
            val result = userService.addUser(user)

            if (result != null) {
                // Stuur een succesbericht terug naar de client, inclusief het nieuwe gebruikers-ID
                call.respond(HttpStatusCode.Created, "User successfully created with id: $result" )
            } else {
                // Stuur een foutmelding als de gebruiker al bestaat in de database (e-mail moet uniek zijn)
                call.respond(HttpStatusCode.Conflict, "User with this email already exists")
            }
        }

        post("/login"){
            // van de data op
            println("Incoming POST request...") // Logbericht voor debugging, toont dat er een verzoek binnenkomt

            // Probeer de body van de aanvraag te lezen en om te zetten naar een User object.
            val user = try {
                call.receive<User>() // Leest JSON-data en converteert deze naar een User object
            } catch (e: Exception) {
                // Als de data niet correct is (bijv. ontbrekende velden), stuur dan een foutmelding terug naar de client
                println("Failed to parse request body: ${e.message}") // Logt de foutmelding
                return@post call.respondText("Invalid data format", status = HttpStatusCode.BadRequest)
            }

            val result = userService.authenticateUser(
                email = user.email,
                password = user.password
            )
            if (result != null) {
                call.respond(HttpStatusCode.OK, "User successfully authenticated")
                
            } else{
                call.respond(HttpStatusCode.BadRequest, "User with this email does not exist")
            }

        }

        // GET-aanvraag: Haal alle voertuigen op
        get("/vehicles") {
            // Roep de service aan om alle voertuigen uit de database te halen en stuur deze als antwoord terug
            call.respond(vehicleService.getAllVehicles())
        }

        // GET-aanvraag: Voertuig bij ID
        get("/vehicles/{id}") {

            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
            // get the vehicle by id here and return this vhehicle
            call.respond(vehicleService.getVehicleByIdV2(id))
        }

        // POST-aanvraag: Voeg een nieuw voertuig toe
        post("/vehicles") {
            println("Incoming POST request...") // Logbericht om het binnenkomende verzoek te tonen

            // Probeer het verzoek om te zetten naar een Vehicle-object
            val vehicle = try {
                call.receive<Vehicle>() // Ontvang de data en zet om naar een Vehicle-object
            } catch (e: Exception) {
                // Als de data niet correct is, stuur een foutmelding terug
                println("Failed to parse request body: ${e.message}") // Logt het probleem
                return@post call.respondText("Invalid data format", status = HttpStatusCode.BadRequest)
            }
            println("Parsed Vehicle: $vehicle") // Log de data van het voertuig ter controle

            // Voeg het voertuig toe aan de database en stuur de data van het nieuwe voertuig terug als antwoord
            val newVehicle = vehicleService.addVehicle(vehicle)
            call.respond(newVehicle)
        }

        // PUT-aanvraag: Werk een bestaand voertuig bij, gebaseerd op een unieke ID
        put("/vehicles/{id}") {
            // Haal het ID van het voertuig uit de URL-parameters en converteer naar een integer
            // Als de conversie mislukt, stuur dan een foutmelding terug
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            // Ontvang en parse de data van het voertuig dat geüpdatet moet worden
            val vehicle = call.receive<Vehicle>()

            // Werk het voertuig bij in de database met het opgegeven ID en data van het voertuig
            if (vehicleService.updateVehicle(id, vehicle)) {
                // Stuur een succesmelding als het voertuig gevonden en bijgewerkt is
                call.respondText("Vehicle updated successfully", status = HttpStatusCode.OK)
            } else {
                // Stuur een foutmelding als het voertuig niet gevonden is in de database
                call.respondText("Vehicle not found", status = HttpStatusCode.NotFound)
            }
        }

        // DELETE-aanvraag: Verwijder een voertuig uit de database op basis van ID
        delete("/vehicles/{id}") {
            // Haal het ID van het voertuig op uit de URL en zet om naar een integer
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

            // Verwijder het voertuig en reageer met een succes- of foutmelding
            if (vehicleService.deleteVehicle(id)) {
                call.respondText("Vehicle deleted successfully", status = HttpStatusCode.OK)
            } else {
                call.respondText("Vehicle not found", status = HttpStatusCode.NotFound)
            }
        }

        // POST-aanvraag: Upload een afbeelding voor een specifiek voertuig, op basis van voertuig-ID
        post("vehicles/images/upload") {

            // Variabele voor de naam van het geüploade bestand
            var imageFileName = ""
            // Ontvang multipart data (meerdere delen van een upload, zoals bestanden)
            val multipartData = call.receiveMultipart()

            // Verwerk elk deel van de multipart data
            multipartData.forEachPart { part ->
                // Check of het deel een bestand is
                if (part is PartData.FileItem) {
                    // Sla de oorspronkelijke bestandsnaam op (of "noImageName" als de naam ontbreekt)
                    imageFileName = part.originalFileName ?: "noImageName"
                    // Converteer de inhoud van het bestand naar een byte-array
                    val fileBytes = part.provider().toByteArray()

                    // Bepaal de volledige bestandslocatie voor opslag
                    val filePath = "uploads/$imageFileName"

                    val directory = File("uploads/")

                    if (!directory.exists()) {
                        directory.mkdirs() // Creëert de map en tussenliggende mappen als die er nog niet zijn
                    }

                    // Schrijf het bestand naar de opgegeven locatie op de server
                    File(filePath).writeBytes(fileBytes)
                }
                // Verwijder het verwerkte deel om geheugen vrij te maken
                part.dispose()
            }

            // Controleer of het bestand is geüpload en stuur een succes- of foutmelding terug
            if (imageFileName.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, getImageUploadPath(imageFileName))
            } else {
                call.respondText("Image not uploaded, something went wrong")
            }
        }

        route("/uploads") {
            get("/{imageName}") {
                val imageName = call.parameters["imageName"] // Haal de naam van de afbeelding op uit de URL
                if (imageName.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Afbeeldingsnaam ontbreekt")
                    return@get
                }

                val file = File("uploads/$imageName") // Pad naar de geüploade bestanden

                if (file.exists() && file.isFile) {
                    call.respondFile(file) // Stuur het bestand terug als response
                } else {
                    call.respond(HttpStatusCode.NotFound, "Afbeelding niet gevonden")
                }
            }
        }


    }
}