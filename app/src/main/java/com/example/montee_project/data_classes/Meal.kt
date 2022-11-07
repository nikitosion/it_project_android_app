package com.example.montee_project.data_classes

import kotlinx.serialization.Serializable


@Serializable
data class Meal(
    var id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val full_time: Int? = null,
    val difficulty: String? = null,
    val likes: Int? = null,
    val comments: Int? = null,
    val ingredients_ids: String? = null,
    val calories: Int? = null,
    val proteins: Int? = null,
    val fats: Int? = null,
    val carbohydrates: Int? = null,
    val diets: String? = null,
    val tags: String? = null,
    var instruction_ids: String? = null
)