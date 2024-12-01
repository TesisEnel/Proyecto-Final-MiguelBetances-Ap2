package edu.ucne.taskmaster.presentation.Calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val download: Download
) : ViewModel() {

    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            download.downloadTasks()
            download.downloadlabels()
            download.downloadTaskLabel(0)
            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth)
                )
            }
        }
    }


    fun changeSelectedDate(selectedDate: Date) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDate = selectedDate,
                    yearMonth = YearMonth.of(selectedDate.year, selectedDate.month)
                )
            }
        }
    }


    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    yearMonth = nextMonth,
                    dates = dataSource.getDates(nextMonth)
                )
            }
        }
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    yearMonth = prevMonth,
                    dates = dataSource.getDates(prevMonth)
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
        val isSelected: Boolean
    ) {
        companion object {
            val Empty = Date("", false)
        }
    }
}
