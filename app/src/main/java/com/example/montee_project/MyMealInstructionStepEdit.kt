package com.example.montee_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.montee_project.data_classes.InstructionStepDB
import com.example.montee_project.database.InstructionStepStorage
import com.example.montee_project.databinding.FragmentMyInstructionStepEditBinding
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "meal_id"
private const val ARG_PARAM2 = "ingredient_id"

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

        fun newInstance(): MyMealIngredientEdit {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInstructionStepEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myInstructionStepDB = Room.databaseBuilder(
            requireContext(),
            InstructionStepStorage::class.java,
            "instruction_step_database"
        ).build()
        val myInstructionStepDao = myInstructionStepDB.instructionStepDao()

        val stepTextInput = binding.stepTextInput
        val confirmButton = binding.confirmButton
        val deleteButton = binding.deleteButton

        var instructionStep: InstructionStepDB? = null

        // Загружаем продукты из локальной базы данных
        if (instructionStepId != null) {
            lifecycleScope.launch {
                instructionStep = myInstructionStepDao.getAllInstructionSteps()
                    .find { it.id == instructionStepId }
            }
        }

        // По нажатию на кнопку добавляется шаг иструкции
        confirmButton.setOnClickListener {
            lifecycleScope.launch {
                val editingInstructionStep = InstructionStepDB(
                    null,
                    "",
                    mealId?.toInt(),
                    stepTextInput.text.toString()
                )

                if (instructionStepId != null && instructionStepId != 0) {
                    editingInstructionStep.id = instructionStep!!.id
                    myInstructionStepDao.editInstructionStep(editingInstructionStep)
                } else {
                    myInstructionStepDao.addInstructionStep(editingInstructionStep)
                }

                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    com.example.montee_project.R.id.nav_host_fragment,
                    MyMealInfo.newInstance(mealId)
                )
                transaction.commit()
            }
        }

        // По нажатию на кнопку удаляется шаг инструкции
        deleteButton.setOnClickListener {
            if (instructionStepId != null) {
                lifecycleScope.launch {
                    val instructionStepsDB = myInstructionStepDao.getAllInstructionSteps()
                    myInstructionStepDao.removeInstructionStep(instructionStepsDB.find { it.id == instructionStepId }!!)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Данного шага не существует. Попробуйте нажать на шаг в списке",
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