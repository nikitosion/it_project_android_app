package com.example.montee_project.data_classes.dao

import androidx.room.*
import com.example.montee_project.data_classes.InstructionStepDB

@Dao
interface InstructionStepDao {

    @Insert
    suspend fun addInstructionStep(InstructionStep: InstructionStepDB)
    @Update
    suspend fun editInstructionStep(InstructionStep: InstructionStepDB)
    @Delete
    suspend fun removeInstructionStep(InstructionStep: InstructionStepDB)

    @Query("SELECT * FROM InstructionSteps")
    suspend fun getAllInstructionSteps(): List<InstructionStepDB>
}