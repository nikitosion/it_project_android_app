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

class ShoppingListFoodAdapter (private val foodList: List<FoodDB>) : RecyclerView.Adapter<ShoppingListFoodAdapter.FoodViewHolder>() {

    class FoodViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val foodName: TextView = view.findViewById(R.id.food_name)
        val needToBuyText: TextView = view.findViewById(R.id.need_to_buy_text)
        val boughtButton: Button = view.findViewById(R.id.bought_button)
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
        holder.needToBuyText.text = "+${foodDB.toBuyAmount}"
//        holder.bind(foodDB, holder.boughtButton.setOnClickListener {  })
    }

    class onButtonClickListener(val clickListener: (button: Button) -> Unit) {
        fun onClick(button: Button) = clickListener(button)
    }

    override fun getItemCount(): Int = foodList.size
}