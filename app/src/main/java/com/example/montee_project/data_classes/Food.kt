package com.example.montee_project.data_classes

data class Food(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val measurement: String? = null,
    val minimal_amount: Double? = null,
    val stock_amount: Double? = null,
    val to_buy_amount: Double? = null,
)