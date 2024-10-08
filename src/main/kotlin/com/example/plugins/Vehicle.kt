package com.example.plugins

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val id: Int = 0, // Zorg dat de ID een standaardwaarde heeft (anders kan deze error veroorzaken)
    val brand: String,
    val model: String,
    val type: String,
    val pricePerDay: Double
)

@Serializable
data class VehicleResponse(
    val message: String,
    val vehicle: Vehicle
)