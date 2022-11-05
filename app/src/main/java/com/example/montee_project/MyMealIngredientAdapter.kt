package com.example.montee_project

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Food
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.IngredientDB
import com.example.montee_project.data_classes.MyFoodDB
import com.squareup.picasso.Picasso


class MyMealIngredientAdapter(private val ingredientList: List<IngredientDB>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MyMealIngredientAdapter.MyMealIngredientViewHolder>() {

    class MyMealIngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Функия для обработки нажатия
        fun bind(myIngredientDB: IngredientDB, itemClickListener: OnItemClickListener) {
            val ingredient_item_string = itemView.context.getString(R.string.ingredient_item_string)
            ingredientText.text = String.format(ingredient_item_string, myIngredientDB.name, myIngredientDB.amount, myIngredientDB.measurement)

            itemView.setOnClickListener {
                itemClickListener.onClick(myIngredientDB)
            }
        }

        val ingredientText: TextView = view.findViewById(R.id.ingredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMealIngredientViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.necessary_ingredients_item, parent, false)

        return MyMealIngredientViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyMealIngredientViewHolder, position: Int) {
        val myIngredientDB = ingredientList[position]

        val ingredient_item_string = holder.itemView.context.getString(R.string.ingredient_item_string)
        holder.ingredientText.text = String.format(ingredient_item_string, myIngredientDB.name, myIngredientDB.amount, myIngredientDB.measurement)

        holder.bind(myIngredientDB, itemClickListener)
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (myIngredient: IngredientDB) -> Unit) {
        fun onClick(myIngredient: IngredientDB) = clickListener(myIngredient)
    }

    override fun getItemCount(): Int = ingredientList.size
}