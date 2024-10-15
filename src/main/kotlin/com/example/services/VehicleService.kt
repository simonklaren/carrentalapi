package com.example.services

import com.example.plugins.Vehicle
import com.example.models.VehicleTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class VehicleService {

    // Haal alle voertuigen op
    fun getAllVehicles(): List<Vehicle> = transaction {
        VehicleTable.selectAll().map { row ->
            Vehicle(
                id = row[VehicleTable.id],
                brand = row[VehicleTable.brand],
                model = row[VehicleTable.model],
                type = row[VehicleTable.type],
                pricePerDay = row[VehicleTable.pricePerDay]
            )
        }
    }

    // Voeg een nieuw voertuig toe
    fun addVehicle(vehicle: Vehicle): Vehicle = transaction {
        val id = VehicleTable.insert { row ->
            row[brand] = vehicle.brand
            row[model] = vehicle.model
            row[type] = vehicle.type
            row[pricePerDay] = vehicle.pricePerDay
        } get VehicleTable.id

        vehicle.copy(id = id)
    }

    // Werk een bestaand voertuig bij
    fun updateVehicle(id: Int, vehicle: Vehicle): Boolean = transaction {
        VehicleTable.update({ VehicleTable.id eq id }) { row ->
            row[brand] = vehicle.brand
            row[model] = vehicle.model
            row[type] = vehicle.type
            row[pricePerDay] = vehicle.pricePerDay
        } > 0
    }

    // Verwijder een voertuig op basis van ID
    fun deleteVehicle(id: Int): Boolean = transaction {
        VehicleTable.deleteWhere { VehicleTable.id eq id } > 0
    }
}
