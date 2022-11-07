package com.example.montee_project.data_classes

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: String? = null,
    val name: String? = null,
    val measurement: String? = null,
    val food_id: String? = null,
    val amount: Float? = null
)