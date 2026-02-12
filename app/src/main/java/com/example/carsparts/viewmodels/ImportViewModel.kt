package com.example.carsparts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.domain.usecase.ImportDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val importDataUseCase: ImportDataUseCase
) : ViewModel() {

    private val _importSuccess = MutableStateFlow<Boolean?>(null)
    val importSuccess: StateFlow<Boolean?> = _importSuccess

    fun importJson(json: String) {
        viewModelScope.launch {
            try {
                importDataUseCase(json)
                _importSuccess.value = true
            } catch (_: Exception) {
                _importSuccess.value = false
            }
        }
    }

    fun clearImportStatus() {
        _importSuccess.value = null
    }
}
