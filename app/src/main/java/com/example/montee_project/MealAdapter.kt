package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Meal

class MealAdapter(private val mealList: List<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealImage: ImageView = view.findViewById(R.id.meal_image)
        val mealName: TextView = view.findViewById(R.id.meal_name)
        val likeButton: ImageView = view.findViewById(R.id.like_icon)
        val likeCounter: TextView = view.findViewById(R.id.likes_counter)
        val cookingTime: TextView = view.findViewById(R.id.cooking_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meal_item, parent, false)

//        val params: GridLayoutManager.LayoutParams = layout.layoutParams as GridLayoutManager.LayoutParams
//        params.width = (parent.measuredWidth / 2) - 8
//        layout.layoutParams = params

        return MealViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealList[position]

        holder.mealImage.setImageResource(R.drawable.carbonara_image)
        holder.mealName.text = meal.name
        holder.likeCounter.text = meal.likes.toString()
        holder.cookingTime.text = meal.full_time.toString()
        holder.likeButton.setOnClickListener {
            /* will be implemented when Database will be connected */
        }
    }

    override fun getItemCount() = mealList.size
}