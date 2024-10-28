package com.example.services

import com.example.dto.UserRegistrationDto
import com.example.models.UserTable
import com.example.utils.* // Voor utilities zoals hashing
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt // Voor het veilig hashen van wachtwoorden


// `UserService` bevat de logica voor gebruikersbeheer, zoals het toevoegen van nieuwe gebruikers en authenticatie.
class UserService {

    // Functie om een nieuwe gebruiker toe te voegen. Deze functie slaat het wachtwoord veilig versleuteld op.
    fun addUser(user: UserRegistrationDto): Int? = transaction {
        // Controleer of er al een gebruiker bestaat met hetzelfde e-mailadres
        val existingUser = UserTable.select { UserTable.email eq user.email }.singleOrNull()

        if (existingUser != null) {
            // Als de gebruiker al bestaat, geef `null` terug als indicatie van een conflict
            null
        } else {
            // Voeg een nieuwe gebruiker toe aan de `UserTable`
            UserTable.insert { row ->
                row[email] = user.email // Sla het e-mailadres op
                row[password] = hashPassword(user.password) // Sla het gehashte wachtwoord op
            } get UserTable.id // Geef het ID van de nieuw toegevoegde gebruiker terug
        }
    }

    // Functie om te controleren of een gebruiker bestaat en de juiste inloggegevens heeft
    fun authenticateUser(email: String, password: String): Boolean = transaction {
        // Haal de gebruiker op uit de database op basis van het e-mailadres
        val user = UserTable.select { UserTable.email eq email }.singleOrNull()

        if (user != null) {
            // Controleer of het opgegeven wachtwoord overeenkomt met het gehashte wachtwoord in de database
            BCrypt.checkpw(password, user[UserTable.password]) // Retourneert `true` als het wachtwoord klopt
        } else {
            false // Retourneert `false` als de gebruiker niet gevonden is
        }
    }
}