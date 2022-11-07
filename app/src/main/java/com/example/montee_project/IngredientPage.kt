package com.example.montee_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.Ingredient
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentIngredientPageBinding
import com.example.montee_project.databinding.FragmentMealReciepePageBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "meal_id"
private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_MEALS_BY_ID = "$BASE_URL/meals/get_meals_by_id"
private const val GET_INGREDIENTS_BY_IDS = "$BASE_URL/ingredients/get_ingredients_by_ids"

class IngredientPage : Fragment() {
    private var mealId: String? = null

    private var _binding: FragmentIngredientPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(mealId: String) =
            IngredientPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, mealId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var meal: Meal
        var portion = 1
        val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val backButton = binding.backButton
        val caloriesText = binding.caloriesText
        val proteinsText = binding.proteinsText
        val fatsText = binding.fatsText
        val carbohydratesText = binding.carbohydratesText
        val minusPortionButton = binding.minusPortionButton
        val plusPortionButton = binding.plusPortionButton
        val portionText = binding.portionText
        val ingredientsList = binding.ingredientsList
        val instructionButton = binding.instructionButton

        backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MealReciepePage.newInstance(mealId.toString()))
            transaction.commit()
        }

        ingredientsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        val portion_string = context?.getString(R.string.portion_string)
        portionText.text = String.format(portion_string.toString(), portion)

        lifecycleScope.launch {
            meal = try {
                client.get(GET_MEALS_BY_ID){
                    url {
                        parameters.append("meal_id", mealId.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                Meal()
            }

            val foodDB =
                Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
            val foodDao = foodDB.foodDao()

            val ingredients: List<Ingredient> = try {
                client.get(GET_INGREDIENTS_BY_IDS) {
                    url {
                        parameters.append("ingredients_ids", meal.ingredients_ids.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                listOf()
            }

            val reqStockFoods = mutableListOf<FoodDB>()
            val stockFoods = foodDao.getAllFoods()
            for (stockFood in stockFoods) {
                for (ingredient in ingredients) {
                    if (ingredient.name == stockFood.foodName) {
                        reqStockFoods.add(stockFood)
                    }
                }
            }

            caloriesText.text = meal.calories.toString()
            proteinsText.text = meal.proteins.toString()
            fatsText.text = meal.fats.toString()
            carbohydratesText.text = meal.carbohydrates.toString()
            ingredientsList.adapter = IngredientAdapter(ingredients, portion, reqStockFoods)
            plusPortionButton.setOnClickListener {
                if (portion >= 1 && portion <= 4) {
                    portion += 1
                    ingredientsList.adapter = IngredientAdapter(ingredients, portion, reqStockFoods)
                    portionText.text = String.format(portion_string.toString(), portion)
                }
            }
            minusPortionButton.setOnClickListener {
                if(portion >= 2 && portion <= 5) {
                    portion -= 1
                    ingredientsList.adapter = IngredientAdapter(ingredients, portion, reqStockFoods)
                    portionText.text = String.format(portion_string.toString(), portion)
                }
            }
        }
    }
}