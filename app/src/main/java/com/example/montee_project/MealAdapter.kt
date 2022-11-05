package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Meal
import com.squareup.picasso.Picasso

class MealAdapter(private val mealList: List<Meal>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MealAdapter.MealViewHolder>(), Filterable {

    private var mealFilterList = listOf<Meal>()

    init {
        mealFilterList = mealList
    }

    class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(meal: Meal, itemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onClick(meal)
            }
        }

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

        return MealViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealFilterList[position]
        if (meal.image != null && meal.image != "") {
            Picasso.get().load(meal.image).fit().centerCrop()
                .placeholder(R.drawable.carbonara_image)
                .into(holder.mealImage)
        } else {
            holder.mealImage.setImageResource(R.drawable.carbonara_image)
        }
        holder.mealName.text = meal.name
        holder.likeCounter.text = meal.likes.toString()
        holder.cookingTime.text = "${meal.full_time.toString()} мин."

        holder.bind(meal, itemClickListener)
    }

    // Фильтрация списка по названию
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    mealFilterList = mealList
                } else {
                    val resultList = ArrayList<Meal>()
                    for (item in mealList) {
                        if (item.name?.lowercase()?.contains(charSearch.lowercase()) == true) {
                            resultList.add(item)
                        }
                    }
                    mealFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = mealFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mealFilterList = results?.values as ArrayList<Meal>
                notifyDataSetChanged()
            }
        }
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (meal: Meal) -> Unit) {
        fun onClick(meal: Meal) = clickListener(meal)
    }


    override fun getItemCount() = mealFilterList.size
}