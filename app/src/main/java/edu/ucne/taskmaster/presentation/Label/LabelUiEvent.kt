package edu.ucne.taskmaster.presentation.Label


sealed interface LabelUiEvent {

    data class DescriptionChange(val description: String) : LabelUiEvent
    data class ColorChange(val color: String) : LabelUiEvent
    data class GetLabel(val id: Int) : LabelUiEvent
    data object SaveLabel : LabelUiEvent
    data class DeleteLabel(val id: Int) : LabelUiEvent
    data object GetLabels : LabelUiEvent
    data object Validate : LabelUiEvent
}
