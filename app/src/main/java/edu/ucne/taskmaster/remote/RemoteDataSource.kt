package edu.ucne.taskmaster.remote

import edu.ucne.taskmaster.remote.api.LabelApi
import edu.ucne.taskmaster.remote.api.TasksApi
import edu.ucne.taskmaster.remote.api.UserApi
import edu.ucne.taskmaster.remote.dto.LabelDto
import edu.ucne.taskmaster.remote.dto.TasksDto
import edu.ucne.taskmaster.remote.dto.UserDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userApi: UserApi,
    private val taskApi: TasksApi,
    private val labelApi: LabelApi
) {

    // User
    suspend fun getUser(id: Int) = userApi.getUser(id)
    suspend fun getAllUser() = userApi.getAllUser()
    suspend fun SaveUser(userDto: UserDto?) = userApi.SaveUser(userDto)
    suspend fun DeleteUser(id: Int) = userApi.DeleteUser(id)

    //Task
    suspend fun getTask(id: Int) = taskApi.getTask(id)
    suspend fun getAllTask() = taskApi.getAllTask()
    suspend fun SaveTask(taskDto: TasksDto?) = taskApi.SaveTask(taskDto)
    suspend fun DeleteTask(id: Int) = taskApi.DeleteTask(id)
    suspend fun getTaskFilterByUser(id: Int) = taskApi.getTaskFilterByUser(id)

    //Label
    suspend fun getLabel(id: Int) = labelApi.getLabel(id)
    suspend fun getLabels() = labelApi.getLabels()
    suspend fun getLabelsFilterByUser(id: Int) = labelApi.getLabelsFilterByUser(id)
    suspend fun SaveLabel(labelDto: LabelDto?) = labelApi.saveLabel(labelDto)
    suspend fun DeleteLabel(id: Int) = labelApi.deleteLabel(id)
}