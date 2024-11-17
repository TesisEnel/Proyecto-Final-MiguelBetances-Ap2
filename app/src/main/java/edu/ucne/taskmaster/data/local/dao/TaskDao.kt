package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Upsert
    suspend fun saveTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(task: TaskEntity) // todo save para insertar o reemplazar

    @Query(
        """
        SELECT * FROM Tasks
        WHERE TaskId = :id
        LIMIT 1
        """
    )
    suspend fun getTask(id: Int): TaskEntity?

    @Query(
        """
        SELECT * FROM Tasks
        """
    )
    suspend fun getAllTask(): List<TaskEntity>

    @Delete
    suspend fun DeleteTask(task: TaskEntity)

    @Query(
        """
        DELETE FROM Tasks
        WHERE TaskId = :id
        """
    )
    suspend fun delete(id: Int)

    @Query("DELETE FROM Tasks WHERE TaskId = :id")
    suspend fun deleteById(id: Int)

}
