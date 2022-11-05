package com.example.montee_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.montee_project.data_classes.FoodDB
import com.example.montee_project.data_classes.IngredientDB
import com.example.montee_project.data_classes.InstructionStepDB
import com.example.montee_project.data_classes.dao.FoodDao
import com.example.montee_project.data_classes.dao.IngredientDao
import com.example.montee_project.data_classes.dao.InstructionStepDao

@Database(
    version = 1,
    entities = [InstructionStepDB::class],
)
abstract class InstructionStepStorage : RoomDatabase() {
    abstract fun instructionStepDao(): InstructionStepDao
}