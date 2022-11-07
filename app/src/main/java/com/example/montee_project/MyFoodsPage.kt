package com.example.montee_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.montee_project.data_classes.MyFoodDB
import com.example.montee_project.database.MyFoodStorage
import com.example.montee_project.databinding.FragmentMyFoodsPageBinding
import kotlinx.coroutines.launch

class MyFoodsPage : Fragment() {

    companion object {
        fun newInstance(): MyFoodsPage {
            return MyFoodsPage()
        }
    }


    private var _binding: FragmentMyFoodsPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyFoodsPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myFoodsList = binding.myFoodsList
        val addFoodButton = binding.addFoodButton

        myFoodsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        lifecycleScope.launch {
            // Подключается база данных, загружаются продукты, и превращаются в класс, который необходим
            // для адаптера
            val myFoodDB = Room.databaseBuilder(
                requireContext(),
                MyFoodStorage::class.java,
                "my_food_database"
            )
                .build()
            val myFoodDao = myFoodDB.foodDao()
            val foodsDB = myFoodDao.getAllFoods()
            val foods = mutableListOf<MyFoodDB>()
            for (food in foodsDB) {
                val newFood = MyFoodDB(
                    food.id,
                    food.foodImage.toString(),
                    food.foodName.toString(),
                    food.measurement.toString()
                )
                foods.add(newFood)
            }
            myFoodsList.adapter = MyFoodAdapter(foods, MyFoodAdapter.OnItemClickListener { myFood ->
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(
                    R.id.nav_host_fragment,
                    MyFoodInfo.newInstance(myFood.id.toString())
                )
                transaction.addToBackStack(null)
                transaction.commit()
            })
        }

        // По нажатию на кнопку добавляется продукт
        addFoodButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MyFoodInfo.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
}