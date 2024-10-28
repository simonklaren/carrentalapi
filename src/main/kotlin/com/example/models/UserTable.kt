package com.example.models

import org.jetbrains.exposed.sql.Table

// Definieert de UserTable-object dat een database tabel "users" representeert in de applicatie.
// Dit maakt gebruik van Exposed, een Kotlin SQL library die ORM-functionaliteit biedt.
object UserTable : Table("users") {
    // Kolom "id": een automatisch oplopend geheel getal, dient als unieke identifier voor elke gebruiker.
    val id = integer("id").autoIncrement()

    // Kolom "email": een tekstveld voor het opslaan van e-mailadressen, met een maximale lengte van 255 tekens.
    // uniqueIndex() zorgt ervoor dat elke e-mail uniek is in de database, dus duplicaten worden voorkomen.
    val email = varchar("email", 255).uniqueIndex()

    // Kolom "password": een tekstveld om het versleutelde wachtwoord op te slaan, met een lengtebeperking van 64 tekens.
    val password = varchar("password", 64)

    // Stelt de primaire sleutel van de tabel in, zodat de kolom "id" de unieke identifier is voor elke rij in "users".
    override val primaryKey = PrimaryKey(id)
}
