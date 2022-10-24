package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentAddFoodPageBinding
import kotlinx.coroutines.launch

private const val BASE_URL = "http://192.168.31.133:3000"
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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addFoodButton = binding.addMealButton
        val foodList = binding.foodList

        addFoodButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, StockFoodInformation.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val foodDB =
            Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
        val foodDao = foodDB.foodDao()

        var foodsDB = listOf<FoodDB>()
        foodList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            foodsDB = foodDao.getAllFoods()
            Log.d(TAG, foodsDB.toString())
            foodList.adapter = FoodAdapter(foodsDB.filter { it.stockAmount > 0 }, FoodAdapter.OnItemClickListener { food ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.nav_host_fragment, StockFoodInformation.newInstance(food.foodName))
                transaction.addToBackStack(null)
                transaction.commit()
            })
        }
    }
}