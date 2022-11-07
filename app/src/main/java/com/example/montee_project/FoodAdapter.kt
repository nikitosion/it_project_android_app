package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.FoodDB
import com.squareup.picasso.Picasso


class FoodAdapter(private val foodList: List<FoodDB>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Функия для обработки нажатия
        fun bind(foodDB: FoodDB, itemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onClick(foodDB)
            }
        }

        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val foodName: TextView = view.findViewById(R.id.food_name)
        val weightText: TextView = view.findViewById(R.id.weight_text)
        val minText: TextView = view.findViewById(R.id.min_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stock_food_item, parent, false)

        return FoodViewHolder(layout)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodDB = foodList[position]

        Picasso.get().load(foodDB.foodImage).into(holder.foodImage)
        holder.foodName.text = foodDB.foodName
        holder.weightText.text = "${foodDB.stockAmount} ${foodDB.measurement}"
        holder.minText.text = "Мин.: ${foodDB.minimalAmount}"

        holder.bind(foodDB, itemClickListener)
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (food: FoodDB) -> Unit) {
        fun onClick(food: FoodDB) = clickListener(food)
    }

    override fun getItemCount(): Int = foodList.size
}