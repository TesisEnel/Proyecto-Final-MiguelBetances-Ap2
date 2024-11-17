package edu.ucne.taskmaster.repository

import android.util.Log
import edu.ucne.taskmaster.data.local.dao.LabelDao
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.remote.RemoteDataSource
import edu.ucne.taskmaster.remote.dto.LabelDto
import edu.ucne.taskmaster.remote.dto.toLabelEntity
import edu.ucne.taskmaster.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LabelRepository @Inject constructor(
    private val labelDao: LabelDao,
    private val labelRemote: RemoteDataSource
) {
    suspend fun getLabelsApi() = labelRemote.getLabels()
    suspend fun saveLabelRoom(label: LabelEntity) = labelDao.save(label)

    fun getLabels(): Flow<Resource<List<LabelEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val labelsApi = labelRemote.getLabels()
            val labelsRoom = labelsApi.map { it.toLabelEntity() }
            labelsRoom.forEach { labelDao.save(it) } // Guardar en la base de datos local

            emit(Resource.Success(labelsRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("LabelRepository", "getLabels: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getLabel(id: Int): Flow<Resource<LabelEntity>> = flow {
        try {
            emit(Resource.Loading())
            val label = labelDao.getLabel(id)
            emit(Resource.Success(label!!))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("LabelRepository", "getLabel: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getLabelsFilterByUser(userId: Int): Flow<Resource<List<LabelEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val labelsApi = labelRemote.getLabelsFilterByUser(userId)
            val labelsRoom = labelsApi.map { it.toLabelEntity() }
            labelsRoom.forEach { labelDao.save(it) } // Guardar en la base de datos local

            emit(Resource.Success(labelsRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("LabelRepository", "getLabelsFilterByUser: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }


    fun saveLabel(label: LabelDto): Flow<Resource<LabelEntity>> = flow {
        try {
            emit(Resource.Loading())
            val labelApi = labelRemote.SaveLabel(label)
            val labelRoom = labelApi.toLabelEntity()
            labelDao.save(labelRoom) // Guardar en la base de datos local

            emit(Resource.Success(labelRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("LabelRepository", "saveLabel: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteLabel(id: Int): Resource<Unit> {
        return try {
            labelRemote.DeleteLabel(id)
            val labelRoom = labelDao.getLabel(id)
            if (labelRoom != null) {
                labelDao.delete(labelRoom) // Eliminar de la base de datos local
            }

            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val labelRoom = labelDao.getLabel(id)
            if (labelRoom != null) {
                labelDao.delete(labelRoom)
            }
            Resource.Success(Unit)
        }
    }
}
