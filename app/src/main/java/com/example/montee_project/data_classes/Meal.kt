package com.example.montee_project.data_classes

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.serialization.Serializable

private const val BASE_URL = "http://10.0.2.2:3000"
private const val GET_INGREDIENTS_BY_IDS = "$BASE_URL/ingredients/get_ingredients_by_ids"
private const val GET_INSTRUCTION_STEPS_BY_IDS = "$BASE_URL/instruction_steps/get_instruction_steps_by_ids"


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
) {
    fun calculateTime() {

    }

    // Служебная функция
    suspend fun getIngredientsList(): List<Ingredient> {
        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }
        val ingredients: List<Ingredient> = try {
            client.get(GET_INGREDIENTS_BY_IDS) {
                url {
                    parameters.append("ingredients_ids", ingredients_ids.toString())
                }
            }.body()
        } catch (e: JsonConvertException) {
            listOf()
        }

        Log.d("Ingredients", ingredients.toString())
        client.close()

        return ingredients
    }
    // Служебная функция
    suspend fun getInstructionStepsList() {
        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }
        val instructionSteps: List<InstructionStep> = try {
            client.get(GET_INSTRUCTION_STEPS_BY_IDS) {
                url {
                    parameters.append("instructionSteps_ids", instruction_ids.toString())
                }
            }.body()
        } catch (e: JsonConvertException) {
            listOf()
        }

        Log.d("Instructions", instructionSteps.toString())
        client.close()
    }
}