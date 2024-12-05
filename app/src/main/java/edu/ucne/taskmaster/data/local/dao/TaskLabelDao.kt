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
    suspend fun deleteTaskLabelsByTaskId(taskId: Int)

    @Query("DELETE FROM LabelTasks WHERE labelId = :labelId")
    suspend fun deleteTaskLabelsByLabelId(labelId: Int)

    @Query(
        """
        SELECT * FROM labeltasks
        """
    )
    suspend fun getAllTaskLabel(): List<TaskLabelEntity>
}
