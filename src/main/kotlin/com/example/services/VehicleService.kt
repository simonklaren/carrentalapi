package com.example.services

import com.example.plugins.Vehicle
import com.example.models.VehicleTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// De `VehicleService` klasse bevat de logica voor het beheren van voertuigen, zoals ophalen, toevoegen, bijwerken en verwijderen
class VehicleService {

    // Functie om alle voertuigen op te halen uit de database
    fun getAllVehicles(): List<Vehicle> = transaction {
        // Voer een SELECT * query uit op de VehicleTable en zet elk resultaat om in een Vehicle-object
        VehicleTable.selectAll().map { row ->
            Vehicle(
                id = row[VehicleTable.id], // Haal het ID op uit de database
                brand = row[VehicleTable.brand], // Haal het merk op
                model = row[VehicleTable.model], // Haal het model op
                type = row[VehicleTable.type], // Haal het type op
                pricePerDay = row[VehicleTable.pricePerDay], // Haal de prijs per dag op
                color = row[VehicleTable.color],
                imageURL = row[VehicleTable.imageURL],
                rentDateFrom = row[VehicleTable.rentDateFrom],
                rentDateTo = row[VehicleTable.rentDateTo],
                lat = row[VehicleTable.lat],
                long = row[VehicleTable.long],
            )
        }
    }

    // getVehicle by id
    fun getVehicleById(id: Int): Boolean = transaction {
       // return the json with this veheicle
        VehicleTable.select { VehicleTable.id eq id }.empty().not()
    }

    fun getVehicleByIdV2(id: Int, ): List<Vehicle> = transaction {
        VehicleTable.select{ VehicleTable.id eq id }.map { row ->
            Vehicle(
                id = row[VehicleTable.id],
                brand = row[VehicleTable.brand],
                model = row[VehicleTable.model],
                type = row[VehicleTable.type],
                pricePerDay = row[VehicleTable.pricePerDay],
                color = row[VehicleTable.color],
                imageURL = row[VehicleTable.imageURL],
                rentDateFrom = row[VehicleTable.rentDateFrom],
                rentDateTo = row[VehicleTable.rentDateTo],
                lat = row[VehicleTable.lat],
                long = row[VehicleTable.long],
            )

        }

    }
    // Functie om een nieuw voertuig toe te voegen aan de database
    fun addVehicle(vehicle: Vehicle): Vehicle = transaction {
        // Voeg een nieuw record toe aan de VehicleTable
        val id = VehicleTable.insert { row ->
            row[brand] = vehicle.brand // Merk van het voertuig
            row[model] = vehicle.model // Model van het voertuig
            row[type] = vehicle.type // Type voertuig, zoals benzine of elektrisch
            row[pricePerDay] = vehicle.pricePerDay // Prijs per dag voor het voertuig
            row[color] = vehicle.color
            row[imageURL] = vehicle.imageURL
            row[rentDateFrom] = vehicle.rentDateFrom
            row[rentDateTo] = vehicle.rentDateTo
            row[lat] = vehicle.lat
            row[long] = vehicle.long
        } get VehicleTable.id // Haal het ID van het nieuw toegevoegde voertuig op

        // Maak een kopie van het voertuig met het nieuwe ID en geef dit terug
        vehicle.copy(id = id)
    }

    // Functie om een bestaand voertuig bij te werken op basis van het ID
    fun updateVehicle(id: Int, vehicle: Vehicle): Boolean = transaction {
        // Update het voertuig in de database waar het ID overeenkomt met het opgegeven ID
        VehicleTable.update({ VehicleTable.id eq id }) { row ->
            row[brand] = vehicle.brand // Werk het merk bij
            row[model] = vehicle.model // Werk het model bij
            row[type] = vehicle.type // Werk het type bij
            row[pricePerDay] = vehicle.pricePerDay // Werk de prijs per dag bij
            row[color] = vehicle.color
            row[imageURL] = vehicle.imageURL
            row[rentDateFrom] = vehicle.rentDateFrom
            row[rentDateTo] = vehicle.rentDateTo
            row[lat] = vehicle.lat
            row[long] = vehicle.lat
        } > 0 // Controleer of er ten minste één record is bijgewerkt; retourneert `true` als succesvol
    }

    // Functie om een voertuig te verwijderen op basis van het ID
    fun deleteVehicle(id: Int): Boolean = transaction {
        // Verwijder het voertuig uit de VehicleTable waar het ID overeenkomt
        VehicleTable.deleteWhere { VehicleTable.id eq id } > 0 // Retourneert `true` als er een voertuig is verwijderd
    }
}
