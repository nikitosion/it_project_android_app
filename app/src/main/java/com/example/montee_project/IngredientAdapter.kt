package com.example.montee_project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.Ingredient


class IngredientAdapter(
    private val ingredientList: List<Ingredient>,
    private val portion: Int,
    private val reqStockFoods: List<FoodDB>? = null
) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ingredientName: TextView = view.findViewById(R.id.ingredient_name_text)
        val ingredientAmount: TextView = view.findViewById(R.id.ingredient_amount_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.inredients_meal_item, parent, false)

        return IngredientViewHolder(layout)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]

        holder.ingredientName.text = ingredient.name

        // Если у количество необходимого для приготовления продукта меньше его количества, которое
        // есть в наличии, то ингредиент подсвечивается красным
        if (ingredient.amount?.toInt() != 0) {
            val measure_string = holder.itemView.context.getString(R.string.measure_string)
            holder.ingredientAmount.text =
                String.format(
                    measure_string,
                    ingredient.amount?.toInt()?.times(portion),
                    ingredient.measurement
                )
            if (reqStockFoods != null && ingredient.name in reqStockFoods.map { it.foodName }) {
                val foundIngredient = reqStockFoods.find { it.foodName == ingredient.name }
                if (foundIngredient?.stockAmount!! < ingredient.amount!!.toInt().times(portion)) {
                    holder.ingredientName.setTextColor(Color.parseColor("#FF665C"))
                    holder.ingredientAmount.setTextColor(Color.parseColor("#FF665C"))
                }
            }
        } else {
            holder.ingredientAmount.text = "По вкусу"
        }
    }

    override fun getItemCount(): Int = ingredientList.size
}