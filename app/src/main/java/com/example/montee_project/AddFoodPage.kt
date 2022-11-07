package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentAddFoodPageBinding
import kotlinx.coroutines.launch

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_FOODS_BY_IDS = "$BASE_URL/foods/get_foods_by_ids"


class AddFoodPage : Fragment() {

    companion object {
        fun newInstance(): AddFoodPage {
            return AddFoodPage()
        }
    }

    private var _binding: FragmentAddFoodPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFoodPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val suggestRecipesButton = binding.suggestRecipesButton
        val addFoodButton = binding.addMealButton
        val foodList = binding.foodList
        // По клику на кнопку "+" открывается фрагмент для редатктирования продукта
        addFoodButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, StockFoodInformation.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        // Объявляется локальная база данных
        val foodDB =
            Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
        val foodDao = foodDB.foodDao()

        var foodsDB: List<FoodDB> = listOf()
        foodList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Загружаются продукты из удалённой базы данных
        lifecycleScope.launch {
            foodsDB = foodDao.getAllFoods()
            Log.d(TAG, foodsDB.toString())
            foodList.adapter = FoodAdapter(foodsDB.filter { it.stockAmount!! > 0 },
                FoodAdapter.OnItemClickListener { food ->
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(
                        R.id.nav_host_fragment,
                        StockFoodInformation.newInstance(food.foodName)
                    )
                    transaction.addToBackStack(null)
                    transaction.commit()
                })
        }

        suggestRecipesButton.setOnClickListener {
            if (foodsDB != listOf<FoodDB>()) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.nav_host_fragment, SearchPage.newInstance("", "", true))
                transaction.commit()
            } else {
                Toast.makeText(requireContext(), "Добавьте продукты!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}