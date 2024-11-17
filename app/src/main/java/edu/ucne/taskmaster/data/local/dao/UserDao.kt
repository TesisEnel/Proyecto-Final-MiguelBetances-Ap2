package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.UserEntity

@Dao
interface UserDao {
    // Obtener todos los usuarios
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<UserEntity>

    // Obtener un usuario por ID
    @Query("SELECT * FROM User WHERE userId = :id LIMIT 1")
    suspend fun getUser(id: Int): UserEntity?

    // Guardar o actualizar un usuario
    @Upsert
    suspend fun saveUser(user: UserEntity)

    // Eliminar un usuario
    @Delete
    suspend fun delete(user: UserEntity)

    // Eliminar un usuario por ID
    @Query("DELETE FROM User WHERE userId = :id")
    suspend fun deleteUserById(id: Int)
}
