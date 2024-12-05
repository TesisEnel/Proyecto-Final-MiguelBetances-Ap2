package edu.ucne.taskmaster.presentation.Label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.taskmaster.remote.dto.LabelDto
import edu.ucne.taskmaster.remote.dto.toLabelEntity
import edu.ucne.taskmaster.repository.LabelRepository
import edu.ucne.taskmaster.repository.TaskLabelRepository
import edu.ucne.taskmaster.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    private val taskLabelRepository: TaskLabelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LabelUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(LabelUiEvent.GetLabels)
    }

    fun onEvent(event: LabelUiEvent) {
        when (event) {
            is LabelUiEvent.SaveLabel -> {
                saveLabelRoom()
            }

            is LabelUiEvent.DeleteLabel -> {
                deleteLabel(event.id)
            }

            is LabelUiEvent.DescriptionChange -> {
                _uiState.update { it.copy(description = event.description) }
            }

            is LabelUiEvent.ColorChange -> {
                _uiState.update {
                    it.copy(hexColor = event.color.removePrefix("#"))
                }
            }

            is LabelUiEvent.GetLabel -> {
                getLabel(event.id)
            }

            is LabelUiEvent.GetLabels -> {
                loadLabelsFromRoom()
            }

            is LabelUiEvent.Validate -> {
                validate()
            }
        }
    }

    private fun loadLabelsFromRoom() {
        viewModelScope.launch {
            val labels = labelRepository.getLabelsRoom()
            _uiState.update {
                it.copy(
                    labels = labels
                )
            }
            onEvent(LabelUiEvent.GetLabels)

        }
    }

    private fun loadLabels() {
        viewModelScope.launch {
            labelRepository.getLabels().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                labels = result.data ?: emptyList()
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error loading labels"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveLabelRoom() {
        viewModelScope.launch {
            val labelDto = LabelDto(
                description = _uiState.value.description,
                hexColor = _uiState.value.hexColor,
                id = if (_uiState.value.id == 0) null else _uiState.value.id
            )
            labelRepository.saveLabelRoom(labelDto.toLabelEntity())
        }
    }


    private fun saveLabel() {
        viewModelScope.launch {
            val labelDto = LabelDto(
                description = _uiState.value.description,
                hexColor = _uiState.value.hexColor,
                id = if (_uiState.value.id == 0) null else _uiState.value.id
            )
            labelRepository.saveLabel(labelDto).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        onEvent(LabelUiEvent.GetLabels)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error saving label"
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteLabel(id: Int) {
        viewModelScope.launch {
            labelRepository.deleteLabelRoom(id)
            taskLabelRepository.deleteTaskLabelByLabelIdRoom(id)
        }
    }

    private fun getLabel(id: Int) {
        viewModelScope.launch {
            val label = labelRepository.getLabelRoom(id)
            _uiState.update {
                it.copy(
                    id = label.id!!,
                    description = label.description,
                    hexColor = label.hexColor
                )
            }
        }
    }

    private fun validate() {
        _uiState.update {
            it.copy(
                descriptionError = if (_uiState.value.description.isBlank()) "La descripción es necesaria" else null
            )
        }
    }
}

