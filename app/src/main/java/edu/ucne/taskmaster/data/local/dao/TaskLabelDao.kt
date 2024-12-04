package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.TaskLabelEntity

@Dao
interface TaskLabelDao {
    @Query("SELECT * FROM LabelTasks WHERE taskId = :taskId")
    suspend fun getTaskLabels(taskId: Int): List<TaskLabelEntity>

    @Upsert
    suspend fun save(taskLabel: TaskLabelEntity)

    @Query("DELETE FROM LabelTasks WHERE taskId = :taskId")
    suspend fun deleteTaskLabels(taskId: Int)
}
