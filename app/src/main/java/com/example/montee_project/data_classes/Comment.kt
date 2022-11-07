package com.example.montee_project.data_classes

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String? = null,
    val meal_id: String? = null,
    val user_id: String? = null,
    val user_name: String? = null,
    val user_image: String? = null,
    val date: String? = null,
    val text: String? = null,
)