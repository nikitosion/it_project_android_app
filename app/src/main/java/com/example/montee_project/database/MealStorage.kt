package com.example.montee_project.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.MealDB
import com.example.montee_project.data_classes.dao.MealDao

@Database(
    version = 1,
    entities = [MealDB::class],
)
abstract class MealStorage : RoomDatabase() {
    abstract fun mealDao(): MealDao
}