package com.example.models

import org.jetbrains.exposed.sql.Table

object VehicleTable : Table("vehicles") {
    val id = integer("id").autoIncrement() // Unieke ID, auto-increment
    val brand = varchar("brand", 50) // Merk van het voertuig
    val model = varchar("model", 50) // Model van het voertuig
    val type = varchar("type", 20) // Type voertuig (ICE, BEV, etc.)
    val pricePerDay = double("price_per_day") // Dagelijkse prijs

    override val primaryKey = PrimaryKey(id) // Primaire sleutel is ID
}
