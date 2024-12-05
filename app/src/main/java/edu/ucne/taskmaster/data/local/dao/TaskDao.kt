package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndGetId(task: TaskEntity): Long

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

    @Query(
        """
        DELETE FROM Tasks
        WHERE TaskId = :id
        """
    )
    suspend fun deleteTask(id: Int)

    @Query("DELETE FROM Tasks WHERE TaskId = :id")
    suspend fun deleteById(id: Int)

    @Query(
        """
        SELECT * 
        FROM Tasks 
        WHERE (:query = '' OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        """
    )
    suspend fun searchTasks(query: String): List<TaskEntity>

    @Query(
        """
        SELECT *
        FROM Tasks
        WHERE TaskId in (:ids) 
        """
    )
    suspend fun getTaskById(ids: List<Int>): List<TaskEntity>

}


