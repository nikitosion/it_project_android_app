package com.example.montee_project.data_classes

data class InstructionStep (
    val necessary_ingredients: List<Ingredient>? = null,
    var step_text: String? = null)