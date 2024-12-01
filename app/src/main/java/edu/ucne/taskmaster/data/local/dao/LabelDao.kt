package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.LabelEntity

@Dao
interface LabelDao {
    @Upsert
    suspend fun upsert(label: LabelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(label: LabelEntity)

    @Query("SELECT * FROM Label")
    suspend fun getAll(): List<LabelEntity>

    @Query("SELECT * FROM Label WHERE id = :id")
    suspend fun getLabel(id: Int): LabelEntity?

    @Delete
    suspend fun delete(label: LabelEntity)
    
}