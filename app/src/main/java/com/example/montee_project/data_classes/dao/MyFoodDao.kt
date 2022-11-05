package com.example.montee_project.data_classes.dao

import androidx.room.*
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.MyFoodDB

@Dao
interface MyFoodDao {

    @Insert
    suspend fun addFood(food: MyFoodDB)
    @Update
    suspend fun editFood(food: MyFoodDB)
    @Delete
    suspend fun removeFood(food: MyFoodDB)

    @Query("SELECT * FROM MyFoodsDB")
    suspend fun getAllFoods(): List<MyFoodDB>
}