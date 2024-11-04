package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.LabelEntity

@Dao
interface LabelDao {
    @Upsert
    suspend fun upsert(label: LabelEntity)

    @Query("SELECT * FROM Label")
    suspend fun getAll(): List<LabelEntity>
}