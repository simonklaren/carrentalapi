package com.example.plugins

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val type: String,  // ICE, BEV, FCEV
    val pricePerDay: Double
)

@Serializable
data class VehicleResponse(
    val message: String,
    val vehicle: Vehicle
)