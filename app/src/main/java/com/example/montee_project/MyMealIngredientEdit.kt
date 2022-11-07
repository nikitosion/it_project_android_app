package com.example.montee_project

import android.os.Bundle
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
import com.example.montee_project.data_classes.IngredientDB
import com.example.montee_project.database.IngredientStorage
import com.example.montee_project.databinding.FragmentMyMealIngredientEditBinding
import com.google.android.material.R
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "meal_id"
private const val ARG_PARAM2 = "ingredient_id"

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_FOODS = "$BASE_URL/foods/get_foods"

class MyMealIngredientEdit : Fragment() {
    private var mealId: String? = null
    private var ingredientId: Int? = null

    companion object {
        fun newInstance(meal_id: String? = null, ingredientId: Int? = null) =
            MyMealIngredientEdit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, meal_id)
                    if (ingredientId != null) {
                        putInt(ARG_PARAM2, ingredientId)
                    }
                }
            }
    }

    private var _binding: FragmentMyMealIngredientEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
            ingredientId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMealIngredientEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myIngredientDB = Room.databaseBuilder(
            requireContext(),
            IngredientStorage::class.java,
            "ingredient_database"
        ).build()
        val myIngredientDao = myIngredientDB.ingredientDao()

        val ingredientDB =
            Room.databaseBuilder(
                requireContext(),
                IngredientStorage::class.java,
                "ingredient_database"
            ).build()
        val ingredientDao = ingredientDB.ingredientDao()


        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        val foodSpinner = binding.foodSelector
        val measurementText = binding.measurementText
        val foodImage = binding.foodImage
        val amountSlider = binding.amountSlider
        val amountValue = binding.amountValue
        val deleteButton = binding.deleteButton
        val confirmButton = binding.confirmButton

        var foods: List<Food> = listOf()
        var ingredient: IngredientDB? = null
        var savedIngredient: IngredientDB? = null
        var ingredientsDB: List<IngredientDB> = listOf()

        // Загружаются продукты из удалённой и локальной баз данных
        lifecycleScope.launch {
            foods =
                try {
                    client.get(GET_FOODS).body()
                } catch (e: JsonConvertException) {
                    listOf()
                }

            ingredientsDB = myIngredientDao.getAllIngredients()

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    foods.map { it.name })
            foodSpinner.adapter = adapter

            // Если был передан id ингредиента, то в списке выбирается он
            if (ingredientId != null) {
                ingredient = myIngredientDao.getAllIngredients().find { it.id == ingredientId }
                foodSpinner.setSelection(foods.indexOf(foods.find { it.id == ingredient?.food_id }))
            } else {
                foodSpinner.setSelection(0)
            }
        }

        // При изменении значения слайдера меняется текст, отображающий его значение
        amountSlider.addOnChangeListener { _, value, _ ->
            amountValue.text = value.toString()
        }

        // При выборе элемента из выпадающего списка, подргужается информация о нём
        foodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                lifecycleScope.launch {
                    Picasso.get().load(foods[p2].image).into(foodImage)
                }

                measurementText.text = foods[p2].measurement
                savedIngredient = ingredientsDB.find { it.name == foods[p2].name }
                if (foods[p2].measurement == "г" || foods[p2].measurement == "мл") {
                    for (slider in listOf(amountSlider)) {
                        slider.valueFrom = 0F
                        slider.valueTo = 1000F
                        slider.value = 100F
                        slider.stepSize = 1F
                    }
                } else if (foods[p2].measurement == "шт" || foods[p2].measurement == "зубч" || foods[p2].measurement == "л" || foods[p2].measurement == "пуч") {
                    for (slider in listOf(amountSlider)) {
                        slider.valueFrom = 0F
                        slider.valueTo = 100F
                        slider.value = 10F
                        slider.stepSize = 1F
                    }
                }

                if (savedIngredient != null) {
                    amountSlider.value = savedIngredient!!.amount!!.toFloat()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                for (slider in listOf(amountSlider)) {
                    slider.valueFrom = 0F
                    slider.valueTo = 10F
                }
                measurementText.text = ""
                amountValue.text = "0.0"
            }
        }

        // По нажатию на кнопку добавляется ингредиент
        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val editingIngredient = IngredientDB(
                    null,
                    foods.find { it.name == foodSpinner.selectedItem.toString() }?.id,
                    mealId?.toInt(),
                    foods.find { it.name == foodSpinner.selectedItem.toString() }?.name,
                    foods.find { it.name == foodSpinner.selectedItem.toString() }?.measurement.toString(),
                    amountSlider.value.toInt()
                )

                if (ingredientId != null && ingredientId != 0) {
                    editingIngredient.id = ingredientId!!.toInt()
                    ingredientDao.editIngredient(editingIngredient)
                } else if (savedIngredient != null) {
                    editingIngredient.id = savedIngredient!!.id
                    ingredientDao.editIngredient(editingIngredient)
                } else {
                    ingredientDao.addIngredient(editingIngredient)
                }

                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    com.example.montee_project.R.id.nav_host_fragment,
                    MyMealInfo.newInstance(mealId.toString())
                )
                transaction.commit()
            }
        }

        // По нажатию на кнопку удаляется ингредиент
        deleteButton.setOnClickListener {
            if (ingredientId != null) {
                lifecycleScope.launch {
                    ingredientDao.removeIngredient(ingredientsDB.find { it.id == ingredientId }!!)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Данного продукта не существует. Попробуйте нажать на продукт в списке",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                com.example.montee_project.R.id.nav_host_fragment,
                MyMealInfo.newInstance(mealId)
            )
            transaction.commit()
        }
    }

}