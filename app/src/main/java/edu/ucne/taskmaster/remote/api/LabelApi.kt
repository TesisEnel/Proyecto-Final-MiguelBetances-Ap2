package edu.ucne.taskmaster.remote.api

import edu.ucne.taskmaster.remote.dto.LabelDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LabelApi {

    @GET("api/Label/{id}")
    suspend fun getLabel(@Path("id") id: Int): LabelDto

    @GET("api/Label/")
    suspend fun getLabels(): List<LabelDto>

    @GET("api/Label/User/{id}")
    suspend fun getLabelsFilterByUser(@Path("id") id: Int): List<LabelDto>

    @POST("api/Label/")
    suspend fun saveLabel(@Body labelDto: LabelDto?): LabelDto

    @DELETE("api/Label/{id}")
    suspend fun deleteLabel(@Path("id") id: Int)
}