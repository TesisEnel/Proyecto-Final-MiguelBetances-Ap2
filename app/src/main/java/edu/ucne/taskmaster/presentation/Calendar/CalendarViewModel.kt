package edu.ucne.taskmaster.presentation.Calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.taskmaster.data.local.entities.TaskEntity
import edu.ucne.taskmaster.repository.TaskRepository
import edu.ucne.taskmaster.util.CalendarDataSource
import edu.ucne.taskmaster.util.Download
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val download: Download,
    private val tasksRepository: TaskRepository,
) : ViewModel() {

    private val dataSource by lazy { CalendarDataSource() }
    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {

            download.downloadTasks()
            download.downloadlabels()
            download.downloadTaskLabel(0)
            val tasksFromDb = tasksRepository.getTaskRoom()
            _tasks.update { tasksFromDb }

            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth, tasksFromDb)
                )
            }

            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth, _tasks.value)
                )
            }
        }
    }


    fun changeSelectedDate(selectedDate: Date) {
        viewModelScope.launch {
            val year = selectedDate.year + 1900
            val month = selectedDate.month + 1

            _uiState.update {
                it.copy(
                    selectedDate = selectedDate,
                    yearMonth = YearMonth.of(year, month),
                    dates = dataSource.getDates(YearMonth.of(year, month), _tasks.value)
                )
            }

        }
    }


    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    yearMonth = nextMonth,
                    dates = dataSource.getDates(nextMonth, _tasks.value)
                )
            }
        }
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    yearMonth = prevMonth,
                    dates = dataSource.getDates(prevMonth, _tasks.value)
                )
            }
        }
    }

    fun onDateClick() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(showModal = !_uiState.value.showModal)
            }
        }
    }
}

data class CalendarUiState(
    val yearMonth: YearMonth,
    val dates: List<Date>,
    val selectedDate: java.util.Date? = null,
    val showModal: Boolean = false
) {
    companion object {
        val Init = CalendarUiState(
            yearMonth = YearMonth.now(),
            dates = emptyList()
        )
    }

    data class Date(
        val dayOfMonth: String,
        val isSelected: Boolean,
        val tasks: List<TaskEntity> = emptyList() // Lista de tareas
    ) {
        companion object {
            val Empty = Date("", false, emptyList())
        }
    }

}
