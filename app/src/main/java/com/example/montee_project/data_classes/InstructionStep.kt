package com.example.montee_project.data_classes

import kotlinx.serialization.Serializable

@Serializable
data class InstructionStep(
    val id: String? = null,
    val ingredients_ids: String? = null,
    var step_text: String? = null
)