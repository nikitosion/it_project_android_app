package com.example.montee_project.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.IngredientDB
import com.example.montee_project.data_classes.dao.IngredientDao

@Database(
    version = 1,
    entities = [IngredientDB::class],
)
abstract class IngredientStorage : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
}