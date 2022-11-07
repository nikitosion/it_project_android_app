package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Meals")
data class MealDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_id")
    val id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "image")
    val image: String? = null,
    @ColumnInfo(name = "full_time")
    val full_time: Int? = null,
    @ColumnInfo(name = "difficulty")
    val difficulty: String? = null,
    @ColumnInfo(name = "likes")
    val likes: Int? = null,
    @ColumnInfo(name = "comments")
    val comments: Int? = null,
    @ColumnInfo(name = "ingredients_ids")
    val ingredients_ids: String? = null,
    @ColumnInfo(name = "calories")
    val calories: Int? = null,
    @ColumnInfo(name = "proteins")
    val proteins: Int? = null,
    @ColumnInfo(name = "fats")
    val fats: Int? = null,
    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Int? = null,
    @ColumnInfo(name = "diets")
    val diets: String? = null,
    @ColumnInfo(name = "instruction_ids")
    var instruction_ids: String? = null
)