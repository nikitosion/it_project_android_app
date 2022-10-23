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
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentStockFoodInformationBinding
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


class StockFoodInformation : Fragment() {

    companion object {
        fun newInstance(): StockFoodInformation {
            return StockFoodInformation()
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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        var adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), 0)
        val foodSpinner = binding.foodSelector
        val foodImage = binding.foodImage

        var foods: List<Food> = listOf()
        lifecycleScope.launch {
            foods =
                try {
                    client.get(GET_FOODS).body()
                } catch (e: JsonConvertException) {
                    listOf()
                }
            adapter =
                ArrayAdapter(requireContext(), R.layout.simple_spinner_item, foods.map { it.name })
            foodSpinner.adapter = adapter
        }

        foodSpinner.adapter = adapter

        val measurementText = binding.measurementText
        measurementText.text = foodSpinner.selectedItem?.toString()

        val amountSlider = binding.amountSlider
        val amountValue = binding.amountValue

        amountSlider.valueFrom = 0F
        amountSlider.valueTo = 10F
        amountValue.text = amountSlider.value.toString()

        val minAmountSlider = binding.minAmountSlider
        val minAmountValue = binding.minAmountValue
        minAmountSlider.valueFrom = 0F
        minAmountSlider.valueTo = 10F
        minAmountValue.text = amountSlider.value.toString()

        amountSlider.addOnChangeListener { _, value, _ -> amountValue.text = value.toString() }

        minAmountSlider.addOnChangeListener { _, value, _ ->
            minAmountValue.text = value.toString()
        }

        foodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lifecycleScope.launch {
                    Picasso.get().load(foods[p2].image).into(foodImage)
                }
                measurementText.text = foods[p2].measurement
                if (foods[p2].measurement == "г") {
                    for (i in listOf(amountSlider, minAmountSlider)) {
                        i.valueFrom = 0F
                        i.valueTo = 1000F
                        i.value = 100F
                        i.stepSize = 1F
                    }
                } else if (foods[p2].measurement == "шт" || foods[p2].measurement == "зубч") {
                    for (i in listOf(amountSlider, minAmountSlider)) {
                        i.valueFrom = 0F
                        i.valueTo = 100F
                        i.value = 10F
                        i.stepSize = 1F
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                measurementText.text = ""
            }
        }

        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            val foodDB =
                Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database")
                    .build()
            val foodDao = foodDB.foodDao()

            val addingFood = foods.find { it.name == foodSpinner.selectedItem.toString() }
            lifecycleScope.launch {
                val addingFoodDB = FoodDB(
                    foodDao.getAllFoods().size + 1,
                    addingFood?.image.toString(),
                    foodSpinner.selectedItem.toString(),
                    String.format("%.1f", minAmountSlider.value.toDouble()).toDouble(),
                    String.format("%.1f", amountSlider.value.toDouble()).toDouble(),
                    0.0
                )
                foodDao.addFood(addingFoodDB)

                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    com.example.montee_project.R.id.nav_host_fragment,
                    AddFoodPage.newInstance()
                )
                transaction.commit()
            }

            val text =
                "Confirmed\n Item ${foodSpinner.selectedItem} was created.\n Stock: ${amountValue.text} ${measurementText.text}\n Min. weight: ${minAmountValue.text} ${measurementText.text}"
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}