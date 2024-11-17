package edu.ucne.taskmaster.remote.api

import edu.ucne.taskmaster.remote.dto.LabelDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LabelApi {

    @GET("api/Labels/{id}")
    suspend fun getLabel(@Path("id") id: Int): LabelDto

    @GET("api/Labels/")
    suspend fun getLabels(): List<LabelDto>

    @GET("api/Labels/User/{id}")
    suspend fun getLabelsFilterByUser(@Path("id") id: Int): List<LabelDto>

    @POST("api/Labels/")
    suspend fun saveLabel(@Body labelDto: LabelDto?): LabelDto

    @DELETE("api/Labels/{id}")
    suspend fun deleteLabel(@Path("id") id: Int)
}