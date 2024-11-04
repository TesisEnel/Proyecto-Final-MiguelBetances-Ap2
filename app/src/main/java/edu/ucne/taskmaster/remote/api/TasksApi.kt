package edu.ucne.taskmaster.remote.api

import edu.ucne.taskmaster.remote.dto.TasksDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TasksApi {
    @GET("api/Tasks/{id}")
    suspend fun getTask(@Path("id") id: Int): TasksDto

    @GET("api/Tasks")
    suspend fun getAllTask(): List<TasksDto>

    @GET("api/Tasks/User/{id}")
    suspend fun getTaskFilterByUser(@Path("id") id: Int): List<TasksDto>

    @POST("api/Tasks")
    suspend fun SaveTask(@Body taskDto: TasksDto?): TasksDto

    @DELETE("api/Tasks/{id}")
    suspend fun DeleteTask(@Path("id") id: Int)
}