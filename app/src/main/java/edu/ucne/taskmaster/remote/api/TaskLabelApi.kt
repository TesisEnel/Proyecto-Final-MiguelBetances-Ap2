package edu.ucne.taskmaster.remote.api

import edu.ucne.taskmaster.remote.dto.TaskLabelDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskLabelApi {

    @GET("/api/LabelTasks/User/{id}")
    suspend fun getLabelTasksUser(@Path("id") id: Int): List<TaskLabelDto>

}