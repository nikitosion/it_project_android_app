package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*


@Entity(tableName = "Foods")
data class FoodDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    val id: Int,
    @ColumnInfo(name = "food_image")
    val foodImage: String,
    @ColumnInfo(name = "food_name")
    val foodName: String,
    @ColumnInfo(name = "minimal_amount")
    val minimalAmount: Double,
    @ColumnInfo(name = "stock_amount")
    val stockAmount: Double,
    @ColumnInfo(name = "to_buy_amount")
    val toBuyAmount: Double
)
