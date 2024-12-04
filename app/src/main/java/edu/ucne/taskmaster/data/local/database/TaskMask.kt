package edu.ucne.taskmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.taskmaster.data.local.dao.LabelDao
import edu.ucne.taskmaster.data.local.dao.TaskDao
import edu.ucne.taskmaster.data.local.dao.TaskLabelDao
import edu.ucne.taskmaster.data.local.dao.UserDao
import edu.ucne.taskmaster.data.local.entities.LabelEntity
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.data.local.entities.TaskLabelEntity
import edu.ucne.taskmaster.data.local.entities.UserEntity

@Database(
    entities = [
        TaskEntity::class,
        UserEntity::class,
        LabelEntity::class,
        TaskLabelEntity::class
    ],
    version = 6,
    exportSchema = false
)


@TypeConverters(Converters::class)
abstract class TaskMask : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun labelDao(): LabelDao
    abstract fun taskLabelDao(): TaskLabelDao
}