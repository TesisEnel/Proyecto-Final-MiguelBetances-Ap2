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
            val labelIds = taskLabelRepository.getTaskLabelRoom(t.taskId).map { it.labelId }
            val labels = labelIds.map { id -> labelRepository.getLabelRoom(id) }
            Tasks(task = t, labels = labels)
        }
    }

}