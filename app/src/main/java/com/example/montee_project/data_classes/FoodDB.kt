package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Foods")
data class FoodDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    val id: Int? = null,
    @ColumnInfo(name = "food_image")
    val foodImage: String? = null,
    @ColumnInfo(name = "food_name")
    val foodName: String? = null,
    @ColumnInfo(name = "measurement")
    val measurement: String? = null,
    @ColumnInfo(name = "minimal_amount")
    val minimalAmount: Int? = null,
    @ColumnInfo(name = "stock_amount")
    var stockAmount: Int? = null,
    @ColumnInfo(name = "to_buy_amount")
    val toBuyAmount: Int? = null
)