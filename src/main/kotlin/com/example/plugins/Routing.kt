package com.example.plugins

import com.example.services.VehicleService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.configureRouting() {
    routing {
        val vehicleService = VehicleService()

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
    }
}