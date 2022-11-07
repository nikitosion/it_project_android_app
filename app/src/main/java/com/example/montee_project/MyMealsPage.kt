package com.example.montee_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.database.MealStorage
import com.example.montee_project.databinding.FragmentMyMealsPageBinding
import kotlinx.coroutines.launch

class MyMealsPage : Fragment() {

    companion object {
        fun newInstance(): MyMealsPage {
            return MyMealsPage()
        }
    }

    private var _binding: FragmentMyMealsPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyMealsPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myMealList = binding.myFoodsList
        val addMealButton = binding.addMealButton

        myMealList.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            // Загружаются блюда и превращаютс в класс, необходимый для адаптера
            val mealDB =
                Room.databaseBuilder(requireContext(), MealStorage::class.java, "meal_database")
                    .build()
            val mealDao = mealDB.mealDao()
            val mealsDB = mealDao.getAllMeals()
            val meals = mutableListOf<Meal>()
            for (meal in mealsDB) {
                val newMeal = Meal(
                    meal.id.toString(),
                    meal.name,
                    meal.image,
                    meal.full_time,
                    meal.difficulty,
                    meal.likes,
                    meal.comments,
                    meal.ingredients_ids,
                    meal.calories,
                    meal.proteins,
                    meal.fats,
                    meal.carbohydrates,
                    meal.diets,
                    "",
                    meal.instruction_ids
                )
                meals.add(newMeal)
            }
            myMealList.adapter = MealAdapter(meals, MealAdapter.OnItemClickListener { meal ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInfo.newInstance(meal.id)
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })
        }

        // По нажатию на кнопку добавляется блюдо
        addMealButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MyMealInfo.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}