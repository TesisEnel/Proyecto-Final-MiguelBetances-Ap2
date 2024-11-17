package edu.ucne.taskmaster.util

import android.util.Log
import edu.ucne.taskmaster.remote.dto.toLabelEntity
import edu.ucne.taskmaster.remote.dto.toTaskEntity
import edu.ucne.taskmaster.repository.LabelRepository
import edu.ucne.taskmaster.repository.TaskRepository
import edu.ucne.taskmaster.repository.UserRepository
import javax.inject.Inject

class Download @Inject constructor(
    private val userRepository: UserRepository,
    private val labelRepository: LabelRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun downloadlabels() {
        try {
            val labels = labelRepository.getLabelsApi()
            labels.forEach {
                labelRepository.saveLabelRoom(
                    it.toLabelEntity()
                )
            }
        } catch (e: Exception) {
            Log.e("Download", "downloadlabels: ${e.message}")
        }

    }

    suspend fun downloadTasks() {
        try {
            val tasks = taskRepository.getTasksApi()
            tasks.forEach {
                taskRepository.saveTaskRoom(
                    it.toTaskEntity()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun downloadUsers() {
        userRepository.getAllUser()
    }
}