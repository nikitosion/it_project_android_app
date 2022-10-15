package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Foods")
data class FoodDB(
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    val id: String,
    @ColumnInfo(name = "minimal_amount")
    val minimalAmount: Double,
    @ColumnInfo(name = "stock_amount")
    val stockAmount: Double,
    @ColumnInfo(name = "to_buy_amount")
    val toBuyAmount: Double
)
