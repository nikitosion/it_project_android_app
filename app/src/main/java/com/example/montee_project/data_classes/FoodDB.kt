package com.example.montee_project.data_classes

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import com.example.montee_project.database.FoodStorage
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