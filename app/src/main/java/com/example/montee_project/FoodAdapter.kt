package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.FoodDB
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*

class FoodAdapter (private val foodList: List<FoodDB>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val foodName: TextView = view.findViewById(R.id.food_name)
        val weightText: TextView = view.findViewById(R.id.weight_text)
        val minusButton: Button = view.findViewById(R.id.minus_button)
        val plusButton: Button = view.findViewById(R.id.plus_button)
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
//        holder.foodImage.setImageResource(R.drawable.salat)
        holder.foodName.text = foodDB.foodName
        holder.weightText.text = foodDB.stockAmount.toString()
        holder.minusButton.setOnClickListener {
            /* minus weight */
        }
        holder.plusButton.setOnClickListener {
            /* plus weight */
        }
    }

    override fun getItemCount(): Int = foodList.size
}