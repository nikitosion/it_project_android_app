package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.data_classes.MealDB
import com.example.montee_project.database.IngredientStorage
import com.example.montee_project.database.InstructionStepStorage
import com.example.montee_project.database.MealStorage
import com.example.montee_project.databinding.FragmentMyMealInfoBinding
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "meal_id"

class MyMealInfo : Fragment() {
    private var mealId: String? = null

    companion object {
        fun newInstance(meal_id: String? = null) =
            MyMealInfo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, meal_id)
                }
            }
    }

    private var _binding: FragmentMyMealInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyMealInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = binding.mealNameInput
        val imageLinkInput = binding.imageLinkInput
        val fullTimeInput = binding.fullTimeInput
        val radioGroupDifficulty = binding.radioGroupDifficult
        var difficulty = ""
        val ingredientsList = binding.ingredientsList
        val addIngredientButton = binding.addIngredientButton
        val calories = binding.caloriesInput
        val fats = binding.fatsInput
        val proteins = binding.proteinsInput
        val carbohydrates = binding.carbohydratesInput
        val sportCheckBox = binding.sportCheckBox
        val veganCheckBox = binding.veganCheckBox
        val dietsList = mutableListOf<String>()
        var diets = ""
        val instructionStepsList = binding.instructionsList
        val addIntstructionStepButton = binding.addInstructionButton
        val confirmButton = binding.confirmButton

        var lastIndexMeal = 0

        // Подключение локальных баз данных
        val mealDB =
            Room.databaseBuilder(requireContext(), MealStorage::class.java, "meal_database")
                .build()
        val mealDao = mealDB.mealDao()

        val myIngredientDB = Room.databaseBuilder(
            requireContext(),
            IngredientStorage::class.java,
            "ingredient_database"
        ).build()
        val myIngredientDao = myIngredientDB.ingredientDao()

        val myInstructionStepDB = Room.databaseBuilder(
            requireContext(),
            InstructionStepStorage::class.java,
            "instruction_step_database"
        ).build()
        val myInstructionStepDao = myInstructionStepDB.instructionStepDao()


        ingredientsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        instructionStepsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        lifecycleScope.launch {
            val meals = mealDao.getAllMeals()
            val emptyMeal =
                meals.find { it.name == null || it.image == null || it.full_time == null || it.difficulty == null || it.calories == null || it.proteins == null || it.fats == null || it.carbohydrates == null}

            // Если во фрагмент передаётся id блюда, то загружаем его из базы данных, или
            // если id не передаётся, но есть созданное пустое (или частично пустое) блюдо, то
            // загружаем его из базы данных
            if (mealId != null || emptyMeal != null) {
                if (mealId != null) {
                    Log.d("id", "I'm in mealId")
                    lastIndexMeal = mealId!!.toInt()
                } else if (emptyMeal != null) {
                    Log.d("id", "I'm in empty")
                    lastIndexMeal = emptyMeal.id
                }
                val loadedMeal = meals.find { it.id == lastIndexMeal }
                Log.d("id", lastIndexMeal.toString())

                nameInput.setText(loadedMeal?.name)
                imageLinkInput.setText(loadedMeal?.image)
                Log.d("Full time", fullTimeInput.toString())
                if (loadedMeal?.full_time != null)
                    fullTimeInput.setText(loadedMeal.full_time.toString())
                when (loadedMeal?.difficulty) {
                    "Лёгкая" -> radioGroupDifficulty.check(R.id.easy_button)
                    "Средняя" -> radioGroupDifficulty.check(R.id.middle_button)
                    "Высокая" -> radioGroupDifficulty.check(R.id.hard_button)
                }
                difficulty = loadedMeal?.difficulty.toString()
                if (loadedMeal?.calories != null)
                    calories.setText(loadedMeal.calories.toString())
                if (loadedMeal?.proteins != null)
                    proteins.setText(loadedMeal.proteins.toString())
                if (loadedMeal?.fats != null)
                    fats.setText(loadedMeal.fats.toString())
                if (loadedMeal?.carbohydrates != null)
                    carbohydrates.setText(loadedMeal.carbohydrates.toString())
                if (loadedMeal?.diets?.contains("sport") == true)
                    sportCheckBox.isChecked = true
                if (loadedMeal?.diets?.contains("vegan") == true)
                    veganCheckBox.isChecked = true
            }
            // Если id не передаётся и нет пустого созданного блюдо, то создаём пустое блюдо
            else {
                mealDao.addMeal(MealDB(0))
                lastIndexMeal = mealDao.getAllMeals().last().id
            }

            addIngredientButton.setOnClickListener {
                lifecycleScope.launch {

                    val ingredientsIds =
                        myIngredientDao.getAllIngredients().filter { it.meal_id == lastIndexMeal }
                            .map { it.id }.joinToString(",")
                    val instructionStepsIds = myInstructionStepDao.getAllInstructionSteps()
                        .filter { it.meal_id == lastIndexMeal }.map { it.id }.joinToString(",")


                    var id = 1
                    if (mealId != null)
                        id = mealId!!.toInt()
                    else if (emptyMeal != null) {
                        id = emptyMeal!!.id
                    }
                    Log.d("id", "Saved id: ${id.toString()}")

                    val editingMeal = MealDB(
                        id,
                        nameInput.text.toString(),
                        imageLinkInput.text.toString(),
                        fullTimeInput.text.toString().toIntOrNull(),
                        difficulty,
                        0,
                        0,
                        ingredientsIds,
                        calories.text.toString().toIntOrNull(),
                        proteins.text.toString().toIntOrNull(),
                        fats.text.toString().toIntOrNull(),
                        carbohydrates.text.toString().toIntOrNull(),
                        diets,
                        instructionStepsIds
                    )
                    Log.d("Meal", editingMeal.toString())
                    mealDao.editMeal(editingMeal)
                }
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealIngredientEdit.newInstance(lastIndexMeal.toString(), null)
                )
                transaction.commit()
            }

            val ingredients =
                myIngredientDao.getAllIngredients().filter { it.meal_id == lastIndexMeal }
            ingredientsList.adapter = MyMealIngredientAdapter(
                ingredients,
                MyMealIngredientAdapter.OnItemClickListener { ingredient ->
                    lifecycleScope.launch {

                        val ingredientsIds =
                            myIngredientDao.getAllIngredients()
                                .filter { it.meal_id == lastIndexMeal }
                                .map { it.id }.joinToString(",")
                        val instructionStepsIds = myInstructionStepDao.getAllInstructionSteps()
                            .filter { it.meal_id == lastIndexMeal }.map { it.id }.joinToString(",")


                        var id = 0
                        if (mealId != null)
                            id = mealId!!.toInt()
                        else if (emptyMeal != null) {
                            id = emptyMeal!!.id
                        }

                        val editingMeal = MealDB(
                            id,
                            nameInput.text.toString(),
                            imageLinkInput.text.toString(),
                            fullTimeInput.text.toString().toIntOrNull(),
                            difficulty,
                            0,
                            0,
                            ingredientsIds,
                            calories.text.toString().toIntOrNull(),
                            proteins.text.toString().toIntOrNull(),
                            fats.text.toString().toIntOrNull(),
                            carbohydrates.text.toString().toIntOrNull(),
                            diets,
                            instructionStepsIds
                        )
                        mealDao.editMeal(editingMeal)

                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.add(
                            R.id.nav_host_fragment,
                            MyMealIngredientEdit.newInstance(
                                lastIndexMeal.toString(),
                                ingredient.id
                            )
                        )
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                })


            addIntstructionStepButton.setOnClickListener {
                lifecycleScope.launch {

                    val ingredientsIds =
                        myIngredientDao.getAllIngredients().filter { it.meal_id == lastIndexMeal }
                            .map { it.id }.joinToString(",")
                    val instructionStepsIds = myInstructionStepDao.getAllInstructionSteps()
                        .filter { it.meal_id == lastIndexMeal }.map { it.id }.joinToString(",")


                    var id = 0
                    if (mealId != null)
                        id = mealId!!.toInt()
                    else if (emptyMeal != null) {
                        id = emptyMeal!!.id
                    }

                    val editingMeal = MealDB(
                        id,
                        nameInput.text.toString(),
                        imageLinkInput.text.toString(),
                        fullTimeInput.text.toString().toIntOrNull(),
                        difficulty,
                        0,
                        0,
                        ingredientsIds,
                        calories.text.toString().toIntOrNull(),
                        proteins.text.toString().toIntOrNull(),
                        fats.text.toString().toIntOrNull(),
                        carbohydrates.text.toString().toIntOrNull(),
                        diets,
                        instructionStepsIds
                    )
                    mealDao.editMeal(editingMeal)
                }

                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyMealInstructionStepEdit.newInstance(lastIndexMeal.toString(), null)
                )
                transaction.commit()
            }

            val instructionSteps =
                myInstructionStepDao.getAllInstructionSteps().filter { it.meal_id == lastIndexMeal }
            instructionStepsList.adapter = MyMealInstructionStepAdapter(
                instructionSteps,
                MyMealInstructionStepAdapter.OnItemClickListener { instructionStep ->
                    lifecycleScope.launch {

                        val ingredientsIds =
                            myIngredientDao.getAllIngredients()
                                .filter { it.meal_id == lastIndexMeal }
                                .map { it.id }.joinToString(",")
                        val instructionStepsIds = myInstructionStepDao.getAllInstructionSteps()
                            .filter { it.meal_id == lastIndexMeal }.map { it.id }.joinToString(",")


                        var id = 0
                        if (mealId != null)
                            id = mealId!!.toInt()
                        else if (emptyMeal != null) {
                            id = emptyMeal!!.id
                        }

                        val editingMeal = MealDB(
                            id,
                            nameInput.text.toString(),
                            imageLinkInput.text.toString(),
                            fullTimeInput.text.toString().toIntOrNull(),
                            difficulty,
                            0,
                            0,
                            ingredientsIds,
                            calories.text.toString().toIntOrNull(),
                            proteins.text.toString().toIntOrNull(),
                            fats.text.toString().toIntOrNull(),
                            carbohydrates.text.toString().toIntOrNull(),
                            diets,
                            instructionStepsIds
                        )
                        mealDao.editMeal(editingMeal)

                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.add(
                            R.id.nav_host_fragment,
                            MyMealInstructionStepEdit.newInstance(
                                lastIndexMeal.toString(),
                                instructionStep.id
                            )
                        )
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                })
        }

        radioGroupDifficulty.setOnCheckedChangeListener { _, checkedId ->
            val selectedItem = radioGroupDifficulty.findViewById<RadioButton>(checkedId)
            difficulty = selectedItem.text.toString()
        }

        sportCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dietsList.add("sport")
            } else {
                dietsList.remove("sport")
            }
            Log.d("diets", "Diets list: ${dietsList.toString()}")
            diets = dietsList.joinToString(",")
            Log.d("diets", "Diets list: $diets")
        }
        veganCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dietsList.add("vegan")
            } else {
                dietsList.remove("vegan")
            }
            Log.d("diets", "Diets list: ${dietsList.toString()}")
            diets = dietsList.joinToString(",")
            Log.d("diets", "Diets list: $diets")
        }

        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val ingredientsIds =
                    myIngredientDao.getAllIngredients().filter { it.meal_id == lastIndexMeal }
                        .map { it.id }.joinToString(",")
                val instructionStepsIds = myInstructionStepDao.getAllInstructionSteps()
                    .filter { it.meal_id == lastIndexMeal }.map { it.id }.joinToString(",")

                val editingMeal = MealDB(
                    0,
                    nameInput.text.toString(),
                    imageLinkInput.text.toString(),
                    fullTimeInput.text.toString().toIntOrNull(),
                    difficulty,
                    0,
                    0,
                    ingredientsIds,
                    calories.text.toString().toIntOrNull(),
                    proteins.text.toString().toIntOrNull(),
                    fats.text.toString().toIntOrNull(),
                    carbohydrates.text.toString().toIntOrNull(),
                    diets,
                    instructionStepsIds
                )
                if (mealId != null) {
                    mealDao.editMeal(editingMeal)
                } else {
                    mealDao.addMeal(editingMeal)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                MyMealsPage.newInstance()
            )
            transaction.commit()
        }
    }
}