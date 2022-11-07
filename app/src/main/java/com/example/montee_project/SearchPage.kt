package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.Ingredient
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.data_classes.MealDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.database.MealStorage
import com.example.montee_project.databinding.FragmentSearchBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_MEALS = "$BASE_URL/meals/get_meals"
private const val GET_INGREDIENTS_BY_IDS = "$BASE_URL/ingredients/get_ingredients_by_ids"

private const val ARG_PARAM1 = "recievedFilterTags"
private const val ARG_PARAM2 = "recievedFilterDiets"
private const val ARG_PARAM3 = "suggested"

class SearchPage : Fragment() {
    private var recievedFilterTags: String? = null
    private var recievedFilterDiets: String? = null
    private var suggested: Boolean? = null

    companion object {
        fun newInstance(tags: String? = "", diets: String? = "", suggested: Boolean) =
            SearchPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, tags)
                    putString(ARG_PARAM2, diets)
                    putBoolean(ARG_PARAM3, suggested)
                }
            }
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        arguments?.let {
            recievedFilterTags = it.getString(ARG_PARAM1)
            recievedFilterDiets = it.getString(ARG_PARAM2)
            suggested = it.getBoolean(ARG_PARAM3)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealDB =
            Room.databaseBuilder(requireContext(), MealStorage::class.java, "meal_database")
                .build()
        val mealDao = mealDB.mealDao()

        val searchView = binding.searchField
        val searchList = binding.searchList

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        var meals: MutableList<Meal>

        searchList.layoutManager = GridLayoutManager(requireContext(), 2)
        var adapter = MealAdapter(listOf(), MealAdapter.OnItemClickListener { meal ->
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                MyMealInfo.newInstance(meal.id)
            )
            transaction.addToBackStack(null)
            transaction.commit()
        })

        // Получаем блюда из удалённой базы данных
        lifecycleScope.launch {
            meals = try {
                client.get(GET_MEALS).body()
            } catch (e: JsonConvertException) {
                mutableListOf()
            }
            val mealsDB = mealDao.getAllMeals()
            for (meal in mealsDB) {
                val newMeal = Meal(
                    meal.id.toString(),
                    meal.name,
                    meal.image,
                    meal.full_time,
                    meal.difficulty,
                    meal.likes,
                    meal.comments,
                    meal.ingredients_ids,
                    meal.calories,
                    meal.proteins,
                    meal.fats,
                    meal.carbohydrates,
                    meal.diets,
                    "",
                    meal.instruction_ids
                )
                meals.add(newMeal)
            }

            // Если получили фильтры (тэги и диеты), то фильтруем изначальный список
            val filteredMeals = mutableListOf<Meal>()
            if (recievedFilterTags != "") {
                val tags = recievedFilterTags!!.split(",")
                for (tag in tags) {
                    filteredMeals.addAll(meals.filter { it.tags?.contains(tag) == true })
                }
            }
            if (recievedFilterDiets != "") {
                val diets = recievedFilterDiets!!.split(",")
                for (diet in diets) {
                    filteredMeals.addAll(meals.filter { it.diets?.contains(diet) == true })
                }
            }
            if (filteredMeals != listOf<Meal>()) {
                meals = filteredMeals.toSet().toMutableList()
            }
            if (suggested == true) {
                Log.d("List", suggested.toString())
                val foodDB =
                    Room.databaseBuilder(
                        requireContext(),
                        FoodStorage::class.java,
                        "food_database"
                    ).build()
                val foodDao = foodDB.foodDao()
                val foodsDB = foodDao.getAllFoods()
                val foodsNames = foodsDB.map { it.foodName }
                val newFilteredList = mutableSetOf<Meal>()
                lifecycleScope.launch {
                    for (meal in meals) {
                        for (ingredient_id in meal.ingredients_ids?.split(",")!!) {
                            val ingredients: List<Ingredient> = try {
                                client.get(GET_INGREDIENTS_BY_IDS) {
                                    url {
                                        parameters.append(
                                            "ingredients_ids",
                                            meal.ingredients_ids.toString()
                                        )
                                    }
                                }.body()
                            } catch (e: JsonConvertException) {
                                listOf()
                            }
                            val ingredient = ingredients.find { it.id == ingredient_id }
                            if (foodsNames.contains(ingredient?.name)) {
                                newFilteredList.add(meal)
                            }
                        }
                    }

                    meals = newFilteredList.plus(meals).toMutableList()

                    adapter = MealAdapter(meals, MealAdapter.OnItemClickListener { meal ->
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.add(
                            R.id.nav_host_fragment,
                            MealReciepePage.newInstance(meal.id.toString())
                        )
                        transaction.addToBackStack(null)
                        transaction.commit()
                    })
                    searchList.adapter = adapter
                }
            } else {
                adapter = MealAdapter(meals, MealAdapter.OnItemClickListener { meal ->
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(
                        R.id.nav_host_fragment,
                        MealReciepePage.newInstance(meal.id.toString())
                    )
                    transaction.addToBackStack(null)
                    transaction.commit()
                })
                searchList.adapter = adapter
            }

            // При изменении запроса в соотв. поле филтруем список
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
            })

            val filtersButton = binding.filtersButton

            // После нажатия кнопки "Фильтры" преходим в фильтры
            filtersButton.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    FiltersInformation.newInstance(suggested == true)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }
}