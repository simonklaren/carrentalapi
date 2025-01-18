package com.example.models

import org.jetbrains.exposed.sql.Table

// Definieert de tabel "vehicles" in de database via het Exposed ORM in Kotlin.
object VehicleTable : Table("vehicles") {
    // Kolom "id": een uniek, automatisch oplopend geheel getal dat elk voertuig identificeert.
    val id = integer("id").autoIncrement() // Unieke ID, auto-increment

    // Kolom "brand": een tekstveld om het merk van het voertuig op te slaan, met een maximale lengte van 50 tekens.
    val brand = varchar("brand", 50) // Merk van het voertuig

    // Kolom "model": een tekstveld om het model van het voertuig op te slaan, met een maximale lengte van 50 tekens.
    val model = varchar("model", 50) // Model van het voertuig

    // Kolom "type": een veld om het type van het voertuig op te slaan, zoals ICE (Internal Combustion Engine) of BEV (Battery Electric Vehicle), met een maximale lengte van 20 tekens.
    val type = varchar("type", 20) // Type voertuig (ICE, BEV, etc.)

    // Kolom "pricePerDay": een double-waarde die de prijs per dag voor het huren van het voertuig aangeeft.
    val pricePerDay = double("price_per_day") // Dagelijkse prijs

    val color = varchar("color", 255)

    val imageURL = varchar("image_url", 255)

    val rentDateFrom = varchar("rent_date_from", 255)

    val rentDateTo = varchar("rent_date_to", 255)

    // Stelt de primaire sleutel van de tabel in, waarbij "id" de unieke identifier is voor elke rij in de "vehicles" tabel.
    override val primaryKey = PrimaryKey(id) // Primaire sleutel is ID
}
