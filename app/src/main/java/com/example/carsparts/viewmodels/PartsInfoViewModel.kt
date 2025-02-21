package com.example.carsparts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.repository.PartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun loadPartInfo(partId: Int) {
        viewModelScope.launch {
            repository.getPartById(partId).collect { partEntity ->
                _part.value = partEntity
            }
        }
    }
}