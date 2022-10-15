package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentAddFoodPageBinding
import com.google.api.Logging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

private const val BASE_URL = "http://10.0.2.2:3000"
private const val GET_UUID = "$BASE_URL/users/"


class AddFoodPage : Fragment() {

    companion object {
        fun newInstance() : AddFoodPage {
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

        val foodDB = Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
        val foodDao = foodDB.foodDao()

        var foods = listOf<FoodDB>()

        lifecycleScope.launch {
//            foods = foodDao.getAllFoods()
            Log.d("Server", "Started")
            val client = HttpClient()
            val data = client.get(GET_UUID)
            Log.d("Server", data.body())
            }
//            Log.d("Server", response.toString())
//            client.close()
        }

//        foodList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        foodList.adapter =
        /* Food list implementation */
}