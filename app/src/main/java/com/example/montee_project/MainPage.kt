package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.json
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentMainPageBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val BASE_URL = "http://192.168.1.44:3000"
private const val GET_MEALS = "$BASE_URL/meals/get_meals"

class MainPage : Fragment() {

    companion object {
        fun newInstance(): MainPage {
            return MainPage()
        }
    }

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var meals: List<Meal>
        // загружаем блюда из удалённой базы данных
        lifecycleScope.launch {
            val client = HttpClient() {
                install(ContentNegotiation) {
                    gson()
                }
            }
            meals = try {
                client.get(GET_MEALS).body()
            } catch (e: JsonConvertException) {
                listOf()
            }

            // Находим блюда для каждого из разделов и добавляем их в сами разделы
            val listOfMonthMeal: RecyclerView = binding.listOfMonthMeal
            listOfMonthMeal.layoutManager = GridLayoutManager(requireContext(), 2)
            val monthMealsItems =
                meals.filter { it.tags?.contains("month") ?: false }.sortedByDescending { it.likes }
                    .take(6)
            listOfMonthMeal.adapter = MealAdapter(monthMealsItems, MealAdapter.OnItemClickListener {meal ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInfo.newInstance(meal.id)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })

            val listOfMorningMeal: RecyclerView = binding.listOfMorningMeal
            listOfMorningMeal.layoutManager = GridLayoutManager(requireContext(), 2)
            val morningMealItems =
                meals.filter { it.tags?.contains("morning") ?: false }
                    .sortedByDescending { it.likes }
                    .take(6)
            listOfMorningMeal.adapter = MealAdapter(morningMealItems, MealAdapter.OnItemClickListener {meal ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInfo.newInstance(meal.id)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })

            val listOfInterestingMeal: RecyclerView = binding.listOfInterestingMeals
            listOfInterestingMeal.layoutManager = GridLayoutManager(requireContext(), 2)
            val interestingMealItems =
                meals.filter { it.tags?.contains("interesting") ?: false }
                    .sortedByDescending { it.likes }
                    .take(6)
            listOfInterestingMeal.adapter = MealAdapter(interestingMealItems,MealAdapter.OnItemClickListener {meal ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInfo.newInstance(meal.id)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })

            val listOfSeasonsMeal: RecyclerView = binding.listOfSeasonsMeal
            listOfSeasonsMeal.layoutManager = GridLayoutManager(requireContext(), 2)
            val seasonMealItems =
                meals.filter { it.tags?.contains("season") ?: false }
                    .sortedByDescending { it.likes }
                    .take(6)
            listOfSeasonsMeal.adapter = MealAdapter(seasonMealItems,MealAdapter.OnItemClickListener {meal ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInfo.newInstance(meal.id)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })
        }
    }

}