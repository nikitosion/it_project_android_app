package com.example.montee_project

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
import com.example.montee_project.data_classes.MyFoodDB
import com.squareup.picasso.Picasso


class MyFoodAdapter(private val foodList: List<MyFoodDB>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MyFoodAdapter.MyFoodViewHolder>() {

    class MyFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Функия для обработки нажатия
        fun bind(myFoodDB: MyFoodDB, itemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onClick(myFoodDB)
            }
        }


        val foodImage: ImageView = view.findViewById(R.id.food_image)
        val foodName: TextView = view.findViewById(R.id.food_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFoodViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.my_food_item, parent, false)

        return MyFoodViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyFoodViewHolder, position: Int) {
        val myFoodDB = foodList[position]

        if (myFoodDB.foodImage != null && myFoodDB.foodImage != "") {
            Picasso.get().load(myFoodDB.foodImage).fit().centerCrop()
                .placeholder(R.drawable.carbonara_image)
                .into(holder.foodImage)
        } else {
            holder.foodImage.setImageResource(R.drawable.carbonara_image)
        }
        holder.foodName.text = myFoodDB.foodName

        holder.bind(myFoodDB, itemClickListener)
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (myFood: MyFoodDB) -> Unit) {
        fun onClick(myFood: MyFoodDB) = clickListener(myFood)
    }

    override fun getItemCount(): Int = foodList.size
}