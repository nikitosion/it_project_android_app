package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.FoodDB
import com.squareup.picasso.Picasso

class ShoppingListFoodAdapter(private val foodList: List<FoodDB>, val itemClickListener: ShoppingListFoodAdapter.OnItemClickListener) :
    RecyclerView.Adapter<ShoppingListFoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Функия для обработки нажатия
        fun bind(foodDB: FoodDB, itemClickListener: ShoppingListFoodAdapter.OnItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onClick(foodDB)
            }
        }

        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val foodName: TextView = view.findViewById(R.id.food_name)
        val needToBuyText: TextView = view.findViewById(R.id.need_to_buy_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.shopping_list_food_item, parent, false)

        return FoodViewHolder(layout)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodDB = foodList[position]

        Picasso.get().load(foodDB.foodImage).into(holder.foodImage)
        holder.foodName.text = foodDB.foodName
        holder.needToBuyText.text = "+${foodDB.toBuyAmount} ${foodDB.measurement}"

        holder.bind(foodDB, itemClickListener)
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (food: FoodDB) -> Unit) {
        fun onClick(food: FoodDB) = clickListener(food)
    }

    override fun getItemCount(): Int = foodList.size
}