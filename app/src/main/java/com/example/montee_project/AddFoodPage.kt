package com.example.montee_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.montee_project.databinding.FragmentAddFoodPageBinding

class AddFoodPage : Fragment() {

    companion object {
        fun newInstance() : AddFoodPage {
            return AddFoodPage()
        }
    }

    private var _binding: FragmentAddFoodPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFoodPageBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addFoodButton = binding.addMealButton
        val foodList = binding.foodList


    }
}