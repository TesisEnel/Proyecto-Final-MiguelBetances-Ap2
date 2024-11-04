package edu.ucne.taskmaster.remote.api

import edu.ucne.taskmaster.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("api/User/{id}")
    suspend fun getUser(@Path("id") id: Int): UserDto

    @GET("api/User")
    suspend fun getAllUser(): List<UserDto>

    @POST("api/User")
    suspend fun SaveUser(@Body userDto: UserDto?): UserDto

    @DELETE("api/User/{id}")
    suspend fun DeleteUser(@Path("id") id: Int)


}