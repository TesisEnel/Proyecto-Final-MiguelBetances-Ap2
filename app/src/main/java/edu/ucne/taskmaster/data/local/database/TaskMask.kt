package edu.ucne.taskmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.taskmaster.data.local.dao.LabelDao
import edu.ucne.taskmaster.data.local.dao.TaskDao
import edu.ucne.taskmaster.data.local.dao.UserDao
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.data.local.entities.UserEntity

@Database(
    entities = [
        TaskEntity::class,
        UserEntity::class,
        LabelEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TaskMask : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun labelDao(): LabelDao
}