package com.example.plugins

import com.example.services.VehicleService
import com.example.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import com.example.dto.*
import io.ktor.http.*
import java.io.File


//must match with the path location of the uploads dir
private fun getImageUploadPath(imagefile: String) = "uploads/$imagefile"

fun Application.configureRouting() {
    routing {
        val vehicleService = VehicleService()
        val userService = UserService()

        // POST: Maak een account aan
        post("/signup") {
            println("Incoming POST request...")
            val userRegistration = try {
                call.receive<UserRegistrationDto>()
            } catch (e: Exception) {
                println("Failed to parse request body: ${e.message}")
                return@post call.respondText("Invalid data format", status = io.ktor.http.HttpStatusCode.BadRequest)
            }

            // Check of gebruiker al bestaat en sla anders op
            val result = userService.addUser(userRegistration)

            if (result != null) {
                call.respond(HttpStatusCode.Created, "User successfully created with id: $result")
            } else {
                call.respond(HttpStatusCode.Conflict, "User with this email already exists")
            }
        }

        // GET: Haal alle voertuigen op
        get("/vehicles") {
            call.respond(vehicleService.getAllVehicles())
        }

        // POST: Voeg een nieuw voertuig toe
        post("/vehicles") {
            println("Incoming POST request...")
            val vehicle = try {
                call.receive<Vehicle>()
            } catch (e: Exception) {
                println("Failed to parse request body: ${e.message}")
                return@post call.respondText("Invalid data format", status = io.ktor.http.HttpStatusCode.BadRequest)
            }
            println("Parsed Vehicle: $vehicle")

            val newVehicle = vehicleService.addVehicle(vehicle)
            call.respond(newVehicle)
        }


        // PUT: Werk een bestaand voertuig bij op basis van ID
        put("/vehicles/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)
            val vehicle = call.receive<Vehicle>()
            if (vehicleService.updateVehicle(id, vehicle)) {
                call.respondText("Vehicle updated successfully", status = io.ktor.http.HttpStatusCode.OK)
            } else {
                call.respondText("Vehicle not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        // DELETE: Verwijder een voertuig op basis van ID
        delete("/vehicles/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)
            if (vehicleService.deleteVehicle(id)) {
                call.respondText("Vehicle deleted successfully", status = io.ktor.http.HttpStatusCode.OK)
            } else {
                call.respondText("Vehicle not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        // POST: Uploaden van image voor vehicles op basis van vehicle ID
        post("/vehicles/{id}/upload") {
            val vehicleId = call.parameters["id"]?.toIntOrNull()
            if (vehicleId == null) {
                call.respondText("Invalid vehicle ID")
                return@post
            }

            var imageFileName = ""
            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                if (part is PartData.FileItem) {
                    imageFileName = part.originalFileName ?: "noImageName"
                    val fileBytes = part.provider().toByteArray()

                    // Correctie voor het file path
                    val filePath = "uploads/vehicles/$vehicleId/$imageFileName"

                    // Maak een directory aan voor het voertuig
                    val directory = File("uploads/vehicles/$vehicleId")
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }

                    // Sla het bestand op
                    File(filePath).writeBytes(fileBytes)
                }
                part.dispose()
            }

            if (imageFileName.isNotEmpty()) {
                call.respond(
                    HttpStatusCode.OK,
                    "$imageFileName is uploaded to ${getImageUploadPath(imageFileName)}"
                )
            } else {
                call.respondText("Image not uploaded, something went wrong")
            }
        }
    }

}
