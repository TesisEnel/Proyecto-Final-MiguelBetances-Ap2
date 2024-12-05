package edu.ucne.taskmaster.repository

import edu.ucne.taskmaster.presentation.TaskList.Tasks
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val taskLabelRepository: TaskLabelRepository,
    private val taskRepository: TaskRepository,
    private val labelRepository: LabelRepository
) {
    suspend fun getTasksWithRealLabels(): List<Tasks> {
        return taskRepository.getTaskRoom().map { t ->
            val labelIds = taskLabelRepository.getTaskLabelRoom(t.taskId!!).map { it.labelId }
            val labels = labelIds.map { id -> labelRepository.getLabelRoom(id) }
            Tasks(task = t, labels = labels)
        }
    }

    suspend fun searchTasksWithRealLabels(query: String): List<Tasks> {
        return taskRepository.searchTaskRoom(query).map { t ->
            val labelIds = taskLabelRepository.getTaskLabelRoom(t.taskId!!).map { it.labelId }
            val labels = labelIds.map { id -> labelRepository.getLabelRoom(id) }
            Tasks(task = t, labels = labels)
        }
    }

    suspend fun getTaskWithRealLabelsById(taskId: Int): Tasks {
        val task = taskRepository.getTaskRoomById(taskId)
        val labelIds = taskLabelRepository.getTaskLabelRoom(taskId).map { it.labelId }
        val labels = labelIds.map { id -> labelRepository.getLabelRoom(id) }
        return Tasks(task = task!!, labels = labels)
    }

    suspend fun getTaskWithRealLabelsByIds(taskIds: List<Int>): List<Tasks> {
        val tasks = taskRepository.getTaskByIdRoom(taskIds)
        return tasks.map { t ->
            val labelIds = taskLabelRepository.getTaskLabelRoom(t.taskId!!).map { it.labelId }
            val labels = labelIds.map { id -> labelRepository.getLabelRoom(id) }
            Tasks(task = t, labels = labels)
        }
    }

}