package com.example.dto

// Data Transfer Object (DTO) voor het registreren van een nieuwe gebruiker
// Dit object bevat de benodigde gegevens om een nieuw account aan te maken
class UserRegistrationDto (
    val email: String, // Het e-mailadres van de gebruiker, gebruikt voor accountidentificatie
    val password: String // Het wachtwoord van de gebruiker, dat veilig wordt opgeslagen in de database
)
