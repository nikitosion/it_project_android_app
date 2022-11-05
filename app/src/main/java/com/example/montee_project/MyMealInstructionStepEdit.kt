package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.Ingredient
import com.example.montee_project.data_classes.IngredientDB
import com.example.montee_project.data_classes.InstructionStepDB
import com.example.montee_project.database.FoodStorage
import com.example.montee_project.database.IngredientStorage
import com.example.montee_project.database.InstructionStepStorage
import com.example.montee_project.database.MealStorage
import com.example.montee_project.databinding.FragmentMyInstructionStepEditBinding
import com.example.montee_project.databinding.FragmentMyMealIngredientEditBinding
import com.example.montee_project.databinding.FragmentProfilePageBinding
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

private const val BASE_URL = "http://192.168.1.44:3000"
private const val GET_FOODS = "$BASE_URL/foods/get_foods"

class MyMealInstructionStepEdit : Fragment() {
    private var mealId: String? = null
    private var instructionStepId: Int? = null

    companion object {
        fun newInstance(meal_id: String? = null, instructionStepId: Int? = null) =
            MyMealInstructionStepEdit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, meal_id)
                    if (instructionStepId != null) {
                        putInt(ARG_PARAM2, instructionStepId)
                    }
                }
            }
        fun newInstance() : MyMealIngredientEdit {
            return MyMealIngredientEdit()
        }
    }

    private var _binding: FragmentMyInstructionStepEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
            instructionStepId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMyInstructionStepEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }
        val stepTextInput = binding.stepTextInput
        val confirmButton = binding.confirmButton

        var instructionStep: InstructionStepDB? = null

        // Загружаем продукты из удалённой и локальной баз данных
        if (instructionStepId != null) {
            lifecycleScope.launch {
                val myInstructionStepDB = Room.databaseBuilder(
                    requireContext(),
                    InstructionStepStorage::class.java,
                    "instruction_step_database"
                ).build()
                val myInstructionStepDao = myInstructionStepDB.instructionStepDao()
                instructionStep = myInstructionStepDao.getAllInstructionSteps()
                    .find { it.id == instructionStepId }
            }
        }

        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val editingInstructionStep = InstructionStepDB(
                    0,
                    "",
                    mealId?.toInt(),
                    stepTextInput.text.toString()
                )

                val myInstructionStepDB =  Room.databaseBuilder(requireContext(), InstructionStepStorage::class.java, "instruction_step_database").build()
                val myInstructionStepDao = myInstructionStepDB.instructionStepDao()

                if(instructionStepId != null && instructionStepId != 0) {
                    editingInstructionStep.id = instructionStep!!.id
                    myInstructionStepDao.editInstructionStep(editingInstructionStep)
                } else {
                    myInstructionStepDao.addInstructionStep(editingInstructionStep)
                }

                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(com.example.montee_project.R.id.nav_host_fragment, MyMealInfo.newInstance(mealId))
                transaction.commit()
            }
        }
    }

}