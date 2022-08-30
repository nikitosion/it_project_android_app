package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentMainPageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.CharBuffer

class MainPage : Fragment() {

    companion object {
        fun newInstance(): MainPage {
            return MainPage()
        }
    }

    private var _binding: FragmentMainPageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore

        val meals: MutableList<Meal> = mutableListOf()

        db.collection("Meal")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val meal = document.toObject<Meal>()
                    meal.id = document.id
                    meals.add(meal)
                    Log.d(TAG, meal.id.toString())
                }

                val listOfMonthMeal: RecyclerView = binding.listOfMonthMeal
                listOfMonthMeal.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (position) {
                                0 -> 2
                                else -> 1
                            }
                        }
                    }
                    spanCount
                }
                listOfMonthMeal.adapter = MealAdapter(meals.take(3))

                val listOfMorningMeal: RecyclerView = binding.listOfMorningMeal
                listOfMorningMeal.layoutManager = GridLayoutManager(requireContext(), 2)
                listOfMorningMeal.adapter = MealAdapter(meals.take(3))
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}