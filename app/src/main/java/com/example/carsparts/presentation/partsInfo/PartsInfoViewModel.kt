package com.example.carsparts.presentation.partsInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.repository.PartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartsInfoViewModel @Inject constructor(
    private val repository: PartRepository
) : ViewModel() {

    private val _part = MutableStateFlow<PartEntity?>(null)
    val part: StateFlow<PartEntity?> = _part

    // Метод для загрузки данных по partId
    fun loadPartInfo(partId: Int) {
        viewModelScope.launch {
            repository.getPartById(partId).collect { partEntity ->
                _part.value = partEntity
            }
        }
    }

    // Метод для редактирования части
    fun editPart(part: PartEntity) {
        viewModelScope.launch {
            // Обновляем данные в репозитории
            repository.updatePart(part)
            // Обновляем локальное состояние
            _part.value = part
        }
    }

    // Метод для получения данных по partId (если нужно)
    fun getPartById(id: Int): Flow<PartEntity?> {
        return repository.getPartById(id)
    }
}