package com.example.montee_project.data_classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "InstructionSteps")
data class InstructionStepDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "instruction_step_id")
    var id: Int,
    @ColumnInfo(name = "ingredients_ids")
    val ingredients_ids: String? = null,
    @ColumnInfo(name = "meal_id")
    val meal_id: Int? = null,
    @ColumnInfo(name = "step_text")
    val step_text: String? = null
)