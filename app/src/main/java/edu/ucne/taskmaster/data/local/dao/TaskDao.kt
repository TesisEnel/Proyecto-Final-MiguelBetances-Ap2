package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.TaskEntity

@Dao
interface TaskDao {
    @Upsert()
    suspend fun SaveTask(task: TaskEntity)

    @Query(
        """
        SELECT * FROM Tasks
        WHERE TaskId = :id
        Limit 1
        """
    )
    suspend fun getTask(id: Int): TaskEntity

    @Query(
        """
        SELECT * FROM Tasks
        
        """
    )
    suspend fun getAllTask(): List<TaskEntity>

    @Delete
    suspend fun DeleteTask(task: TaskEntity)

}