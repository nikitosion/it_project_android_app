package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.montee_project.databinding.FragmentFiltersInformationBinding

private const val ARG_PARAM1 = "suggested"

class FiltersInformation : Fragment() {
    private var suggested: Boolean? = null

    companion object {
        fun newInstance(suggested: Boolean) =
            FiltersInformation().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, suggested)
                }
            }
    }

    private var _binding: FragmentFiltersInformationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersInformationBinding.inflate(inflater, container, false)

        arguments?.let {
            suggested = it.getBoolean(ARG_PARAM1)
        }

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

        // Если чек-бокс isChecked == true, то добавляется тег или диета в соотв. массив
        for (i in mealsCheckBoxes.indices) {
            mealsCheckBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tags.add(suggestedTags[i])
                } else {
                    tags.remove(suggestedTags[i])
                }
            }
        }
        for (i in dietsCheckBoxes.indices) {
            dietsCheckBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    diets.add(suggestedDiets[i])
                } else {
                    diets.remove(suggestedDiets[i])
                }
            }
        }

        // По нажатию на кнопку "Подтвердить", применяем фильтры и переходим обратно - на страницу поиска
        val confirmButton = binding.confirmButton
        confirmButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.nav_host_fragment,
                SearchPage.newInstance(tags.joinToString(","), diets.joinToString(","),
                    suggested == true
                )
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}