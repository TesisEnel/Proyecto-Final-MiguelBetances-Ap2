package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<UserEntity>

    @Upsert
    suspend fun saveUser(user: UserEntity)
}