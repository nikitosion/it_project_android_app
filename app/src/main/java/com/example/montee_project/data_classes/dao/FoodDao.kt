package com.example.montee_project.data_classes.dao

import androidx.room.*
import com.example.montee_project.data_classes.FoodDB

@Dao
interface FoodDao {

    @Insert
    suspend fun addFood(food: FoodDB)

    @Update
    suspend fun editFood(food: FoodDB)

    @Delete
    suspend fun removeFood(food: FoodDB)

    @Query("SELECT * FROM foods")
    suspend fun getAllFoods(): List<FoodDB>

    @Update
    fun plusMinusWeightFood(food: FoodDB)
}