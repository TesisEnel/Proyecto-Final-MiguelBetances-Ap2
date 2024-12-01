package edu.ucne.taskmaster.repository

import android.util.Log
import edu.ucne.taskmaster.data.local.dao.UserDao
import edu.ucne.taskmaster.data.local.entities.UserEntity
import edu.ucne.taskmaster.remote.RemoteDataSource
import edu.ucne.taskmaster.remote.dto.UserDto
import edu.ucne.taskmaster.remote.dto.toUserEntity
import edu.ucne.taskmaster.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userRemote: RemoteDataSource
) {

    // Obtener un solo usuario
    fun getUser(id: Int): Flow<Resource<UserEntity>> = flow {
        try {
            emit(Resource.Loading())
            val userApi = userRemote.getUser(id)
            val userRoom = userApi.toUserEntity()
            userDao.saveUser(userRoom)

            emit(Resource.Success(userRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("UserRepository", "getUser: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    // Obtener todos los usuarios
    fun getAllUser(): Flow<Resource<List<UserEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val usersApi = userRemote.getAllUser()
            val usersRoom = usersApi.map { it.toUserEntity() }
            usersRoom.forEach { userDao.saveUser(it) }

            emit(Resource.Success(usersRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("UserRepository", "getAllUser: ${e.message}")
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    // Guardar o actualizar un usuario
    fun saveUser(userDto: UserDto): Flow<Resource<UserEntity>> = flow {
        try {
            emit(Resource.Loading())
            val userApi = userRemote.SaveUser(userDto)
            val userRoom = userApi.toUserEntity()
            userDao.saveUser(userRoom)

            emit(Resource.Success(userRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("UserRepository", "saveUser: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    // Eliminar un usuario
    suspend fun deleteUser(id: Int): Resource<Unit> {
        return try {
            userRemote.DeleteUser(id)
            val userRoom = userDao.getUser(id)
            if (userRoom != null) {
                userDao.delete(userRoom)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            Log.e("UserRepository", "deleteUser: ${e.message}")

            val userRoom = userDao.getUser(id)
            if (userRoom != null) {
                userDao.delete(userRoom)
            }
            Resource.Success(Unit)
        }
    }
}
