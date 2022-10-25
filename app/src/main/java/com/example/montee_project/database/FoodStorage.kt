package com.example.montee_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.dao.FoodDao

@Database(
    version = 1,
    entities = [FoodDB::class],
)
abstract class FoodStorage : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}