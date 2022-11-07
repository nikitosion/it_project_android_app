package com.example.montee_project.data_classes.dao

import androidx.room.*
import com.example.montee_project.data_classes.IngredientDB

@Dao
interface IngredientDao {

    @Insert
    suspend fun addIngredient(Ingredient: IngredientDB)

    @Update
    suspend fun editIngredient(Ingredient: IngredientDB)

    @Delete
    suspend fun removeIngredient(Ingredient: IngredientDB)

    @Query("SELECT * FROM Ingredients")
    suspend fun getAllIngredients(): List<IngredientDB>
}