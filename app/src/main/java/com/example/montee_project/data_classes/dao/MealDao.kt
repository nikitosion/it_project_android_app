package com.example.montee_project.data_classes.dao

import androidx.room.*
import com.example.montee_project.data_classes.MealDB

@Dao
interface MealDao {

    @Insert
    suspend fun addMeal(Meal: MealDB)
    @Update
    suspend fun editMeal(Meal: MealDB)
    @Delete
    suspend fun removeMeal(Meal: MealDB)

    @Query("SELECT * FROM Meals")
    suspend fun getAllMeals(): List<MealDB>
}