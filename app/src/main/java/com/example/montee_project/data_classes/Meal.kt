package com.example.montee_project.data_classes


data class Meal(
    var id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val full_time: Int? = null,
    val difficulty: String? = null,
    val likes: Int? = null,
    val comments: Int? = null,
    val ingredients: List<Ingredient>? = null,
    val calories: Int? = null,
    val proteins: Int? = null,
    val fats: Int? = null,
    val carbohydrates: Int? = null,
    val diets: List<String>? = null,
    val tags: List<String>? = null,
    var instruction: List<InstructionStep>? = null) {

    fun calculateTime() {

    }
}