package edu.ucne.taskmaster.presentation.Label

import edu.ucne.taskmaster.remote.dto.LabelDto


sealed interface LabelUiEvent {

    data class DescriptionChange(val description: String) : LabelUiEvent
    data class ColorChange(val color: String) : LabelUiEvent
    data class GetLabel(val id: Int) : LabelUiEvent
    data class SaveLabel(val label: LabelDto) : LabelUiEvent
    data class DeleteLabel(val id: Int) : LabelUiEvent
    data object GetLabels : LabelUiEvent
}
