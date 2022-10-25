package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.montee_project.databinding.FragmentFiltersInformationBinding

class FiltersInformation : Fragment() {
    companion object {
        fun newInstance(): FiltersInformation {
            return FiltersInformation()
        }
    }

    private var _binding: FragmentFiltersInformationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersInformationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Чек-боксы из XML
        val monthMealsCheckBox = binding.monthMealsCheckBox
        val morningMealsCheckBox = binding.morningMealsCheckBox
        val interestingMealsCheckBox = binding.interestingMealsCheckBox
        val seasonMealsCheckBox = binding.seasonMealsCheckBox

        val veganCheckBox = binding.veganCheckBox
        val sportCheckBox = binding.sportCheckBox

        // Массивы для выбранных тегов и диет
        val tags = mutableListOf<String>()
        val diets = mutableListOf<String>()

        val mealsCheckBoxes = listOf(
            monthMealsCheckBox,
            morningMealsCheckBox,
            interestingMealsCheckBox,
            seasonMealsCheckBox
        )
        val dietsCheckBoxes = listOf(veganCheckBox, sportCheckBox)

        val suggestedTags = listOf("month", "morning", "interesting", "season")
        val suggestedDiets = listOf("vegan", "sport")

        // Если чек-бокс isChecked добавляется тег или диету в соотв. массив
        for (i in mealsCheckBoxes.indices) {
            mealsCheckBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tags.add(suggestedTags[i])
                } else {
                    tags.remove(suggestedTags[i])
                }
                Log.d("checkers", "$tags $diets")
            }
        }
        for (i in dietsCheckBoxes.indices) {
            dietsCheckBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    diets.add(suggestedDiets[i])
                } else {
                    diets.remove(suggestedDiets[i])
                }
                Log.d("checkers", "$tags $diets")
            }
        }

        // По нажатию на кнопку "Подтвердить" переходим обратно - на страницу поиска
        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                SearchPage.newInstance(tags.joinToString(","), diets.joinToString(","))
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}