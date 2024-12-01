package edu.ucne.taskmaster.util

import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.presentation.Calendar.CalendarUiState
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class CalendarDataSource {

    fun getDates(yearMonth: YearMonth, tasks: List<TaskEntity>?): List<CalendarUiState.Date> {
        val tasksByDate = getTasksForMonth(yearMonth, tasks)
        return (1..yearMonth.lengthOfMonth()).map { day ->
            val date = yearMonth.atDay(day)
            CalendarUiState.Date(
                dayOfMonth = day.toString(),
                isSelected = false,
                tasks = tasksByDate[date] ?: emptyList()
            )
        }
    }

    private fun getTasksForMonth(
        yearMonth: YearMonth,
        tasks: List<TaskEntity>?
    ): Map<LocalDate, List<TaskEntity>> {
        // Filtrar tareas del mes y agruparlas por fecha
        if (tasks == null) return emptyMap()
        return tasks.filter { task ->
            val taskDate = task.dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            taskDate.year == yearMonth.year && taskDate.monthValue == yearMonth.monthValue
        }.groupBy { task ->
            task.dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}