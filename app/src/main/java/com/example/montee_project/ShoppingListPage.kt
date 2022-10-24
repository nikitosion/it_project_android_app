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
import com.example.montee_project.databinding.FragmentShoppingListPageBinding
import kotlinx.coroutines.launch


class ShoppingListPage : Fragment() {
    companion object {
        fun newInstance(): ShoppingListPage {
            return ShoppingListPage()
        }
    }

    private var _binding: FragmentShoppingListPageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addFoodButton = binding.addFoodButton
        val foodList = binding.foodList

        addFoodButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, ShoppingListFoodInformation.newInstance())
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
            foodsDB = foodDao.getAllFoods().filter { it.toBuyAmount > 0 }
            Log.d(TAG, foodsDB.toString())
            foodList.adapter = ShoppingListFoodAdapter(foodsDB)
        }
    }
}