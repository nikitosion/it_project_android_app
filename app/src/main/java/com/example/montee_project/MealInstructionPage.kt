package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.InstructionStep
import com.example.montee_project.data_classes.InstructionStepDB
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentMealInstructionPageBinding
import com.example.montee_project.databinding.FragmentMealReciepePageBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "meal_id"
private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_MEALS_BY_ID = "$BASE_URL/meals/get_meals_by_id"
private const val GET_INSTRUCTION_STEPS_BY_IDS = "$BASE_URL/instruction_steps/get_instruction_steps_by_ids"

class MealInstructionPage : Fragment() {
    private var mealId: String? = null
    private var _binding: FragmentMealInstructionPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(meal_id: String) =
            MealInstructionPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, meal_id)
                }
            }
    }

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
        _binding = FragmentMealInstructionPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.backButton
        val instructionStepsList = binding.instructionStepsList
        var meal: Meal
        val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MealReciepePage.newInstance(mealId.toString()))
            transaction.commit()
        }

        instructionStepsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        lifecycleScope.launch {
            // Загружаем продукт
            meal = try {
                client.get(GET_MEALS_BY_ID) {
                    url {
                        parameters.append("meal_id", mealId.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                Meal()
            }

            // Загружается инструкция (шаги) из базы данных по id из продукта и подставляется в соотв.
            // места
            val instructionSteps: List<InstructionStep> = try {
                client.get(GET_INSTRUCTION_STEPS_BY_IDS) {
                    url {
                        parameters.append("instructionSteps_ids", meal.instruction_ids.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                listOf()
            }
            Log.d("List",instructionSteps.toString() )
            instructionStepsList.adapter = InstructionAdapter(instructionSteps)
        }
    }
}