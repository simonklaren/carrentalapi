package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Een lijst van voertuigen om mee te beginnen
val vehicleList = mutableListOf(
    Vehicle(1, "Toyota", "Corolla", "ICE", 50.0),
    Vehicle(2, "Tesla", "Model 3", "BEV", 100.0),
    Vehicle(3, "Hyundai", "Nexo", "FCEV", 80.0)
)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/vehicles") {
            call.respond(vehicleList)
        }

        post("/vehicles") {
            try {
                val newVehicle = call.receive<Vehicle>()
                vehicleList.add(newVehicle)
//                call.respond(mapOf("vehicle" to newVehicle))
                call.respond(newVehicle)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.localizedMessage}")
            }
        }

        put("/vehicles/{id}") {
            val vehicleId = call.parameters["id"]?.toIntOrNull()

            if (vehicleId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid vehicle ID")
                return@put
            }

            val updatedVehicle = call.receive<Vehicle>()
            val vehicleIndex = vehicleList.indexOfFirst { it.id == vehicleId }

            if (vehicleIndex == -1) {
                call.respond(HttpStatusCode.NotFound, "Vehicle with ID $vehicleId not found")
                return@put
            }

            vehicleList[vehicleIndex] = updatedVehicle
            // Gebruik de `VehicleResponse` data class voor de response
//            call.respond(VehicleResponse("Vehicle updated successfully", updatedVehicle))
            call.respond(updatedVehicle)
        }

        delete("/vehicles/{id}") {
            val vehicleId = call.parameters["id"]?.toIntOrNull()

            if (vehicleId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid vehicle ID")
                return@delete
            }

            // Zoek naar het bestaande voertuig in de lijst
            val vehicleIndex = vehicleList.indexOfFirst { it.id == vehicleId }

            if (vehicleIndex == -1) {
                call.respond(HttpStatusCode.NotFound, "Vehicle with ID $vehicleId not found")
                return@delete
            }

            // Verwijder het voertuig uit de lijst
            val removedVehicle = vehicleList.removeAt(vehicleIndex)
//            call.respond(VehicleResponse("Vehicle deleted successfully", removedVehicle))
            call.respond(removedVehicle)
        }


    }
}
