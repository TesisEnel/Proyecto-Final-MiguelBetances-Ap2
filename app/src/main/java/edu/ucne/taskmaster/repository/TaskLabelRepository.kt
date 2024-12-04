package edu.ucne.taskmaster.repository

import edu.ucne.taskmaster.data.local.dao.TaskLabelDao
import edu.ucne.taskmaster.data.local.entities.TaskLabelEntity
import edu.ucne.taskmaster.remote.api.TaskLabelApi
import javax.inject.Inject

class TaskLabelRepository @Inject constructor(
    private val taskLabelApi: TaskLabelApi,
    private val tasklabelDao: TaskLabelDao
) {
    suspend fun getTaskLabelApi(id: Int) = taskLabelApi.getLabelTasksUser(id)
    suspend fun saveTaskLabelRoom(taskLabel: TaskLabelEntity) = tasklabelDao.save(taskLabel)
    suspend fun getTaskLabelRoom(taskId: Int) = tasklabelDao.getTaskLabels(taskId)
    suspend fun deleteTaskLabelRoom(taskId: Int) = tasklabelDao.deleteTaskLabels(taskId)

}