package com.example.montee_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.montee_project.data_classes.MyFoodDB
import com.example.montee_project.database.MyFoodStorage
import com.example.montee_project.databinding.FragmentMyFoodInfoBinding
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "food_id"

class MyFoodInfo : Fragment() {
    private var foodId: String? = null

    companion object {
        fun newInstance(food_id: String? = null) =
            MyFoodInfo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, food_id)
                }
            }
    }

    private var _binding: FragmentMyFoodInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodId = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyFoodInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myFoodDB = Room.databaseBuilder(requireContext(), MyFoodStorage::class.java, "my_food_database")
            .build()
        val myFoodDao = myFoodDB.foodDao()

        val foodNameInput = binding.foodNameInput
        val imageLinkInput = binding.imageLinkInput
        val radioGroupMeasurement = binding.radioGroupMeasurement
        var measurement = ""
        val deleteButton = binding.deleteButton
        val confirmButton = binding.confirmButton

        var lastIndexFood = 0
        var emptyFood: MyFoodDB?
        var foods: List<MyFoodDB> = listOf()

        radioGroupMeasurement.setOnCheckedChangeListener { _, checkedId ->
            val selectedItem = radioGroupMeasurement.findViewById<RadioButton>(checkedId)
            measurement = selectedItem.text.toString()
        }

        lifecycleScope.launch {
            foods = myFoodDao.getAllFoods()
            emptyFood =
                foods.find { it.foodImage == null || it.foodName == null || it.measurement == null}

            // Если во фрагмент передаётся id продукта, то загружаем его из базы данных, или
            // если id не передаётся, но есть созданный пустой (или частично пустой) продукт, то
            // загружаем его из базы данных
            if (foodId != null || emptyFood != null) {
                if (foodId != null) {
                    lastIndexFood = foodId!!.toInt()
                } else if (emptyFood != null) {
                    lastIndexFood = emptyFood!!.id!!
                }
                val loadedFood = foods.find { it.id == lastIndexFood }

                foodNameInput.setText(loadedFood?.foodName)
                imageLinkInput.setText(loadedFood?.foodImage)
                when (loadedFood?.measurement) {
                    "кг" -> radioGroupMeasurement.check(R.id.kg_button)
                    "г" -> radioGroupMeasurement.check(R.id.g_button)
                    "л" -> radioGroupMeasurement.check(R.id.l_button)
                    "мл" -> radioGroupMeasurement.check(R.id.ml_button)
                    "шт" -> radioGroupMeasurement.check(R.id.sht_button)
                    "зубч" -> radioGroupMeasurement.check(R.id.zub_button)
                    "пуч" -> radioGroupMeasurement.check(R.id.puch_button)
                }
            }
        }
        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val editingFood = MyFoodDB(
                    null,
                    imageLinkInput.text.toString(),
                    foodNameInput.text.toString(),
                    measurement
                )

                if (foodId != null) {
                    myFoodDao.editFood(editingFood)
                }

                else {
                    myFoodDao.addFood(editingFood)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                MyFoodsPage.newInstance()
            )
            transaction.commit()
        }
        deleteButton.setOnClickListener {
            if (foodId != null) {
                lifecycleScope.launch {
                    myFoodDao.removeFood(foods.find { it.id == foodId?.toInt() }!!)
                }
            } else {
                Toast.makeText(requireContext(), "Данного продукта не существует. Попробуйте нажать на продукт в списке", Toast.LENGTH_SHORT).show()
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                MyFoodsPage.newInstance()
            )
            transaction.commit()
        }
    }
}