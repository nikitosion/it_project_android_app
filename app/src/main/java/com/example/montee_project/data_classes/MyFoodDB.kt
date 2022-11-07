package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MyFoodsDB")
data class MyFoodDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    var id: Int? = null,
    @ColumnInfo(name = "food_image")
    val foodImage: String? = null,
    @ColumnInfo(name = "food_name")
    val foodName: String? = null,
    @ColumnInfo(name = "measurement")
    val measurement: String? = null,
)