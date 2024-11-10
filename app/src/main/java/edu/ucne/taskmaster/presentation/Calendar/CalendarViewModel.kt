package edu.ucne.taskmaster.presentation.Calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.taskmaster.util.CalendarDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


@HiltViewModel
class CalendarViewModel @Inject constructor(

) : ViewModel() {

    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth)
                )
            }
        }
    }


    fun changeSelectedDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDate = selectedDate,
                    yearMonth = YearMonth.of(selectedDate.year, selectedDate.monthValue)
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
    val selectedDate: LocalDate? = null,
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
