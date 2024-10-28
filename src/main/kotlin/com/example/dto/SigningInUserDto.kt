package com.example.dto

// Data Transfer Object (DTO) voor het inloggen van een gebruiker
// Dit object bevat alleen de gegevens die nodig zijn om een gebruiker te verifiëren
class SigningInUserDto (
    val email: String, // Het e-mailadres van de gebruiker
    val password: String // Het wachtwoord van de gebruiker
)
