package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Ingredients")
data class IngredientDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredient_id")
    var id: Int? = null,
    @ColumnInfo(name = "food_id")
    val food_id: String? = null,
    @ColumnInfo(name = "meal_id")
    val meal_id: Int? = null,
    @ColumnInfo(name = "ingredient_name")
    val name: String? = null,
    @ColumnInfo(name = "measurement")
    val measurement: String? = null,
    @ColumnInfo(name = "amount")
    val amount: Int? = null
)