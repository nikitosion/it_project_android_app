package com.example.montee_project

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentShoppingListFoodInformationBinding
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val BASE_URL = "http://192.168.31.133:3000"
private const val GET_FOODS = "$BASE_URL/foods/get_foods"

private const val ARG_PARAM1 = "recievedFoodName"

class ShoppingListFoodInformation : Fragment() {

    private var recievedFoodName: String? = null

    companion object {
        @JvmStatic
        fun newInstance(foodName: String? = null) =
            ShoppingListFoodInformation().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, foodName)
                }
            }
    }

    private var _binding: FragmentShoppingListFoodInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListFoodInformationBinding.inflate(inflater, container, false)

        arguments?.let {
            recievedFoodName = it.getString(ARG_PARAM1)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        val foodDB =
            Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
        val foodDao = foodDB.foodDao()

        val foodSpinner = binding.foodSelector
        val foodImage = binding.foodImage
        var foodsDB = listOf<FoodDB>()

        var foods: List<Food> = listOf()
        lifecycleScope.launch {
            foods =
                try {
                    client.get(GET_FOODS).body()
                } catch (e: JsonConvertException) {
                    listOf()
                }
            Log.d("list", foods.toString())
            foodsDB = foodDao.getAllFoods().filter { it.toBuyAmount > 0 }

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    foods.map { it.name })
            foodSpinner.adapter = adapter
            if (recievedFoodName != null) {
                foodSpinner.setSelection(foods.indexOf(foods.find { it.name == recievedFoodName }))
            }
        }

        val toBuyAmountSlider =
            binding.toBuyAmountSlider // сладейр количества товаров, которые нужно купить
        val toBuyAmountValue = binding.toBuyAmountValue // значение количества товаров, которые нужно купить
        val stockAmountText = binding.stockAmountText // значение количества товара в наличии
        stockAmountText.text = "0"

        toBuyAmountSlider.addOnChangeListener { _, value, _ -> toBuyAmountValue.text = value.toString() }


        foodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                lifecycleScope.launch {
                    Picasso.get().load(foods[p2].image).into(foodImage)
                }

                val stockFood = foodsDB.find { it.foodName == foods[p2].name }
                if (stockFood != null) {
                    stockAmountText.text = "0 ${stockFood.stockAmount}"
                } else {
                    stockAmountText.text = "0 ${foods[p2].measurement}"
                }
                if (foods[p2].measurement == "г" || foods[p2].measurement == "мл") {
                    toBuyAmountSlider.valueFrom = 0F
                    toBuyAmountSlider.valueTo = 1000F
                    toBuyAmountSlider.value = 100F
                    toBuyAmountSlider.stepSize = 1F
                } else if (foods[p2].measurement == "шт" || foods[p2].measurement == "зубч") {
                    toBuyAmountSlider.valueFrom = 0F
                    toBuyAmountSlider.valueTo = 100F
                    toBuyAmountSlider.value = 10F
                    toBuyAmountSlider.stepSize = 1F
                }
                if (stockFood != null) {
                    toBuyAmountSlider.value = stockFood.toBuyAmount.toFloat()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                toBuyAmountSlider.valueFrom = 0F
                toBuyAmountSlider.valueTo = 100F
                toBuyAmountSlider.value = 10F
                toBuyAmountSlider.stepSize = 1F
                toBuyAmountValue.text = "0.0"
            }
        }

        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val editingFood =
                    foods.find { it.name == foodSpinner.selectedItem.toString() } // редактируемый продукты
                val stockFood =
                    foodsDB.find { it.foodName == editingFood?.name } // продукт в налчии

                Log.d("Food", stockFood.toString())
                if (stockFood != null) {
                    val updatingFoodDB = FoodDB(
                        stockFood.id,
                        stockFood.foodImage,
                        stockFood.foodName,
                        stockFood.measurement,
                        stockFood.minimalAmount,
                        stockFood.stockAmount,
                        toBuyAmountSlider.value.toInt()
                    )
                    foodDao.editFood(updatingFoodDB)
                } else {
                    val addingFoodDB = FoodDB(
                        0,
                        editingFood?.image.toString(),
                        editingFood?.name.toString(),
                        editingFood?.measurement.toString(),
                        0,
                        0,
                        toBuyAmountSlider.value.toInt()
                    )
                    foodDao.addFood(addingFoodDB)
                }
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    com.example.montee_project.R.id.nav_host_fragment,
                    ShoppingListPage.newInstance()
                )
                transaction.commit()
            }

            val text =
                "Confirmed\n Item ${foodSpinner.selectedItem} was created.\n Stock: ${toBuyAmountValue.text} ${stockAmountText.text}\n"
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }

    }
}