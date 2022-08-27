package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentMainPageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

class MainPage : Fragment() {

    companion object {
        fun newInstance(): MainPage {
            return MainPage()
        }
    }

    private var binding: FragmentMainPageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMainPageBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val db = Firebase.firestore

        db.collection("Meal")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val meal = document.toObject<Meal>()
                    meal.id = document.id
                    Log.d(TAG, meal.id.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}