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
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentSearchBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val BASE_URL = "http://192.168.31.133:3000"
private const val GET_MEALS = "$BASE_URL/meals/get_meals"

private const val ARG_PARAM1 = "recievedFilterTags"
private const val ARG_PARAM2 = "recievedFilterDiets"

class SearchPage : Fragment() {
    private var recievedFilterTags: String? = null
    private var recievedFilterDiets: String? = null

    companion object {
        @JvmStatic
        fun newInstance(tags: String? = "", diets: String? = "") =
            SearchPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, tags)
                    putString(ARG_PARAM2, diets)
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
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchField
        val searchList = binding.searchList

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        var meals: List<Meal> = listOf()

        searchList.layoutManager = GridLayoutManager(requireContext(), 2)
        var adapter = MealAdapter(listOf())

        // Получаем блюда из удалённой базы данных
        lifecycleScope.launch {
            meals = try {
                client.get(GET_MEALS).body()
            } catch (e: JsonConvertException) {
                listOf()
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
                meals = filteredMeals.toSet().toList()
            }
            adapter = MealAdapter(meals)
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
            transaction.add(R.id.nav_host_fragment, FiltersInformation.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}