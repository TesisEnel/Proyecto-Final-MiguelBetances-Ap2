package edu.ucne.taskmaster.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.taskmaster.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<UserEntity>


    @Query("SELECT * FROM User WHERE userId = :id LIMIT 1")
    suspend fun getUser(id: Int): UserEntity?


    @Upsert
    suspend fun saveUser(user: UserEntity)


    @Delete
    suspend fun delete(user: UserEntity)


    @Query("DELETE FROM User WHERE userId = :id")
    suspend fun deleteUserById(id: Int)
}
