package com.example.montee_project

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
import com.example.montee_project.data_classes.MyFoodDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.database.MyFoodStorage
import com.example.montee_project.databinding.FragmentStockFoodInformationBinding
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_FOODS = "$BASE_URL/foods/get_foods"

private const val ARG_PARAM1 = "recievedFoodName"

class StockFoodInformation : Fragment() {

    private var recievedFoodName: String? = null

    companion object {
        fun newInstance(foodName: String? = null) =
            StockFoodInformation().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, foodName)
                }
            }
    }

    private var _binding: FragmentStockFoodInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockFoodInformationBinding.inflate(inflater, container, false)

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
            Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database")
                .build()
        val foodDao = foodDB.foodDao()

        val foodSpinner = binding.foodSelector
        val foodImage = binding.foodImage
        val deleteFoodButton = binding.deleteButton
        var foodsDB = listOf<FoodDB>()

        var foods: MutableList<Food> = mutableListOf()

        // Загружаем продукты из удалённой и локальной баз данных
        lifecycleScope.launch {
            foods =
                try {
                    client.get(GET_FOODS).body()
                } catch (e: JsonConvertException) {
                    mutableListOf()
                }

            foodsDB = foodDao.getAllFoods().filter { it.stockAmount!! > 0 }

            val myFoodDB = Room.databaseBuilder(requireContext(), MyFoodStorage::class.java, "my_food_database")
                .build()
            val myFoodDao = myFoodDB.foodDao()
            val myFoodsDB = myFoodDao.getAllFoods()
            for (myFood in myFoodsDB) {
                val newFood = Food(
                    myFood.id.toString(),
                    myFood.foodName,
                    myFood.foodImage,
                    myFood.measurement,
                    0.0,
                    0.0,
                    0.0
                )
                foods.add(newFood)
            }

            // Состовляем и применяем список для выпадающего меню
            val adapter =
                ArrayAdapter(
                    requireContext(),
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    foods.map { it.name })
            foodSpinner.adapter = adapter

            // Если был выбран опредёлнный продукт, то он автоматически выбирается
            if (recievedFoodName != null) {
                foodSpinner.setSelection(foods.indexOf(foods.find { it.name == recievedFoodName }))
            }
        }

        val measurementText = binding.measurementText
        val inStockAmountSlider =
            binding.inStockAmountSlider
        val inStockAmountValue = binding.amountValue
        val minAmountSlider = binding.minAmountSlider
        val minAmountValue = binding.minAmountValue

        // Если значение значения слайдреов изменяются, то изменяются тексты в TextView, отвеячающие
        // за отображение соотв. значения
        inStockAmountSlider.addOnChangeListener { _, value, _ ->
            inStockAmountValue.text = value.toString()
        }
        minAmountSlider.addOnChangeListener { _, value, _ ->
            minAmountValue.text = value.toString()
        }

        // При выборе продукта из выпадающего списка, подгружается иноформация про него
        foodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                lifecycleScope.launch {
                    Picasso.get().load(foods[p2].image).into(foodImage)
                }

                measurementText.text = foods[p2].measurement
                val stockFood = foodsDB.find { it.foodName == foods[p2].name }
                if (foods[p2].measurement == "г" || foods[p2].measurement == "мл") {
                    for (slider in listOf(inStockAmountSlider, minAmountSlider)) {
                        slider.valueFrom = 0F
                        slider.valueTo = 1000F
                        slider.value = 100F
                        slider.stepSize = 1F
                    }
                } else if (foods[p2].measurement == "шт" || foods[p2].measurement == "зубч") {
                    for (slider in listOf(inStockAmountSlider, minAmountSlider)) {
                        slider.valueFrom = 0F
                        slider.valueTo = 100F
                        slider.value = 10F
                        slider.stepSize = 1F
                    }
                }
                if (stockFood != null) {
                    inStockAmountSlider.value = stockFood.stockAmount!!.toFloat()
                    minAmountSlider.value = stockFood.minimalAmount!!.toFloat()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                for (slider in listOf(inStockAmountSlider, minAmountSlider)) {
                    slider.valueFrom = 0F
                    slider.valueTo = 10F
                }
                measurementText.text = ""
                inStockAmountValue.text = "0.0"
                minAmountValue.text = "0.0"
            }
        }

        // После нажатися кнопки "Подтвердить" либо добавляется новый продукты,
        // если он не существовал в локаольно базе данных, либо изменяется
        // уже существующий
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
                        minAmountSlider.value.toInt(),
                        inStockAmountSlider.value.toInt(),
                        stockFood.toBuyAmount
                    )
                    foodDao.editFood(updatingFoodDB)
                } else {
                    val addingFoodDB = FoodDB(
                        null,
                        editingFood?.image.toString(),
                        editingFood?.name.toString(),
                        editingFood?.measurement.toString(),
                        minAmountSlider.value.toInt(),
                        inStockAmountSlider.value.toInt(),
                        0
                    )
                    foodDao.addFood(addingFoodDB)
                }
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    AddFoodPage.newInstance()
                )
                transaction.commit()
            }
        }
        deleteFoodButton.setOnClickListener {
            val stockFood = foodsDB.find { it.foodName == foodSpinner.selectedItem.toString() }
            if (stockFood != null) {
                lifecycleScope.launch {
                    foodDao.removeFood(stockFood)
                }
            } else {
                Toast.makeText(requireContext(), "Вы ещё не добавили этот продукт", Toast.LENGTH_SHORT).show()
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                AddFoodPage.newInstance()
            )
            transaction.commit()
        }
    }
}