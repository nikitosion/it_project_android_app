package com.example.montee_project.data_classes

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val measurement: String? = null,
    val minimal_amount: Double? = 0.0,
    val stock_amount: Double? = 0.0,
    val to_buy_amount: Double? = 0.0,
)