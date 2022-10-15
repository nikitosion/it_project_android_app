package com.example.montee_project

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.databinding.FragmentStockFoodInformationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class StockFoodInformation: Fragment() {

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
    ): View? {
        _binding = FragmentStockFoodInformationBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cesar = Food("pyFhlUuaNiSAKOfBK7sk", "Салат «Айсберг»", null, "кг", 0.0, 0.0, 0.0)
        val carrot = Food("5N8t1dofc7KsqYELY3Hj", "Морковь", null, "кг", 0.0, 0.0, 0.0)
        val spagetti = Food("I7YqEkpJgPczMl7wLZ24", "Спагетти", null, "г", 0.0, 0.0, 0.0)
        val parmezan = Food("LKsFGePXzC526g9u8zt1", "Сыр пармезан", null, "кг", 0.0, 0.0, 0.0)
        val pomidori = Food("wvcivYrmyy7J8RZjvSX0", "Помидоры", null, "кг", 0.0, 0.0, 0.0)

        val foods = listOf(cesar, carrot, spagetti, parmezan, pomidori)

        val foodSpinner = binding.foodSelector
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, foods.map { it.name })
        foodSpinner.adapter = adapter

        val measurementText = binding.measurementText
        measurementText.text = foodSpinner.selectedItem.toString()

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

        amountSlider.addOnChangeListener { _, value, _ -> amountValue.text = value.toString()}

        minAmountSlider.addOnChangeListener { _, value, _ -> minAmountValue.text = value.toString()}

        foodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                measurementText.text = foods[p2].measurement
                if (foods[p2].measurement == "г") {
                    amountSlider.valueFrom = 0F
                    amountSlider.valueTo = 1000F
                    amountSlider.value = 100F

                    minAmountSlider.valueFrom = 0F
                    minAmountSlider.valueTo = 1000F
                    minAmountSlider.value = 100F
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                measurementText.text = ""
            }
        }

        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            val foodDB = Room.databaseBuilder(requireContext(), FoodStorage::class.java, "food_database").build()
            val foodDao = foodDB.foodDao()

            val text = "Confirmed\n Item ${foodSpinner.selectedItem} was created.\n Stock: ${amountValue.text} ${measurementText.text}\n Min. weight: ${minAmountValue.text} ${measurementText.text}"
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            val addingFood = foods.find { it.name == foodSpinner.selectedItem.toString()}
            val addingFoodDB = FoodDB(addingFood?.id.toString(), minAmountSlider.value.toDouble(), amountSlider.value.toDouble(), 0.0)
            lifecycleScope.launch {
                    foodDao.addFood(addingFoodDB)
            }
        }
    }
}