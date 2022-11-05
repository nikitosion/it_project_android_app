package com.example.montee_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.MyFoodDB
import com.example.montee_project.data_classes.dao.FoodDao
import com.example.montee_project.data_classes.dao.MyFoodDao

@Database(
    version = 1,
    entities = [MyFoodDB::class],
)
abstract class MyFoodStorage : RoomDatabase() {
    abstract fun foodDao(): MyFoodDao
}