package edu.ucne.taskmaster.repository

import android.util.Log
import androidx.paging.PagingSource
import edu.ucne.taskmaster.data.local.dao.TaskDao
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.remote.RemoteDataSource
import edu.ucne.taskmaster.remote.dto.TasksDto
import edu.ucne.taskmaster.remote.dto.toTaskEntity
import edu.ucne.taskmaster.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskRemote: RemoteDataSource
) {

    fun getTasksPaging(limit: Int, offset: Int): PagingSource<Int, TaskEntity> =
        taskDao.getTasksPaging(limit, offset)

    suspend fun getTasksApi() = taskRemote.getAllTask()

    suspend fun saveTaskRoom(taskEntity: TaskEntity) {
        taskDao.save(taskEntity)
    }

    suspend fun getTaskRoom() = taskDao.getAllTask()


    fun getTasks(): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val tasksApi = taskRemote.getAllTask()
            val tasksRoom = tasksApi.map { it.toTaskEntity() }
            tasksRoom.forEach { taskDao.save(it) }

            emit(Resource.Success(tasksRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("TaskRepository", "getAllTasks: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getTask(id: Int): Flow<Resource<TaskEntity>> = flow {
        try {
            emit(Resource.Loading())
            val task = taskDao.getTask(id)
            if (task == null) {
                emit(Resource.Error("Tarea no encontrada"))
            } else {
                emit(Resource.Success(task))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("TaskRepository", "getTask: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }


    fun getTasksFilterByUser(userId: Int): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val tasksApi = taskRemote.getTaskFilterByUser(userId)
            val tasksRoom = tasksApi.map { it.toTaskEntity() }
            tasksRoom.forEach { taskDao.save(it) }

            emit(Resource.Success(tasksRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("TaskRepository", "getTasksFilterByUser: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun saveTask(taskDto: TasksDto): Flow<Resource<TaskEntity>> = flow {
        try {
            emit(Resource.Loading())
            val taskApi = taskRemote.SaveTask(taskDto)
            val taskRoom = taskApi.toTaskEntity()
            taskDao.save(taskRoom)
            emit(Resource.Success(taskRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("TaskRepository", "saveTask: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteTask(id: Int): Resource<Unit> {
        return try {
            taskRemote.DeleteTask(id)
            taskDao.deleteById(id)
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            Log.e("TaskRepository", "deleteTask: ${e.message}")
            Resource.Error(e.message ?: "Error desconocido")
        }
    }
}
