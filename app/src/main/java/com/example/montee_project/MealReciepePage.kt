package com.example.montee_project

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentMealReciepePageBinding
import com.example.montee_project.databinding.FragmentProfilePageBinding
import com.squareup.picasso.Picasso
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
private const val PUT_MEAL_LIKE_BY_ID = "$BASE_URL/meals/update_meal_like"

class MealReciepePage : Fragment() {
    private var mealId: String? = null
    private var _binding: FragmentMealReciepePageBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(meal_id: String) =
            MealReciepePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, meal_id)
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
        _binding = FragmentMealReciepePageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var meal: Meal
        val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
        }
        val mealName = binding.mealName
        val mealImage = binding.mealImage
        val mealTimeText = binding.mealTimeText
        val difficultyText = binding.difficultyText
        val ingredientsAmount = binding.ingredientsAmountText
        val vegetatrianIcon = binding.vegetarianIcon
        val sportsIcon = binding.sportsIcon
        val sugarIcon = binding.sugarIcon
        val commentButton = binding.commentButton
        val commentText = binding.commentText
        val likeButton = binding.likeButton
        val likeText = binding.likeText
        val caloriesText = binding.caloriesText
        val proteinsText = binding.proteinsText
        val fatsText = binding.fatsText
        val carbohydratesText = binding.carbohydratesText
        val ingredientsButton = binding.ingreientsButton
        val instructionButton = binding.instructionButton

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

            mealName.text = meal.name
            if (meal.image != null && meal.image != "") {
                Picasso.get().load(meal.image).placeholder(R.drawable.carbonara_image).fit().centerCrop().into(mealImage)
            }
            val time_string = context?.getString(R.string.time_string)
            mealTimeText.text = String.format(time_string.toString(), meal.full_time)
            difficultyText.text = meal.difficulty
            ingredientsAmount.text = meal.ingredients_ids?.split(",")?.size.toString()

            if (meal.diets?.contains("vegan") != true) {
                vegetatrianIcon.setColorFilter(R.color.white, PorterDuff.Mode.MULTIPLY)
            }
            if (meal.diets?.contains("sport") != true) {
                sportsIcon.setColorFilter(R.color.white, PorterDuff.Mode.MULTIPLY)
            }
            if (meal.diets?.contains("sugar") == true) {
                sugarIcon.setColorFilter(R.color.white, PorterDuff.Mode.MULTIPLY)
            }

            commentText.text = meal.comments.toString()
            likeText.text = meal.likes.toString()
            caloriesText.text = meal.calories.toString()
            proteinsText.text = meal.proteins.toString()
            fatsText.text = meal.fats.toString()
            carbohydratesText.text = meal.carbohydrates.toString()
        }

        commentButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, CommentsPage.newInstance(mealId.toString()))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        likeButton.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val userId = sharedPref?.getString("USER_ID", "")

            if (userId != "") {
                lifecycleScope.launch {
                    val newMeal: Meal = try {
                        client.put(PUT_MEAL_LIKE_BY_ID) {
                            url {
                                parameters.append("meal_id", mealId.toString())
                                parameters.append("user_id", userId.toString())
                            }
                        }.body()
                    } catch (e: JsonConvertException) {
                        Meal()
                    }
                    likeText.text = newMeal.likes.toString()
                }
            } else {
                Toast.makeText(requireContext(), "Вы не авторизованы!", Toast.LENGTH_LONG).show()
            }
        }

        ingredientsButton.setOnClickListener {
            // переход на страницу с ингредиентами
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, IngredientPage.newInstance(mealId.toString()))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        instructionButton.setOnClickListener {
            // переход на страницу с инструкцией
        }
    }
}