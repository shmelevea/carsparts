package com.example.carsparts.viewmodels

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
class PartsViewModel @Inject constructor(
    private val partRepository: PartRepository
) : ViewModel() {

    private val _parts = MutableStateFlow<List<PartEntity>>(emptyList())
    val parts: StateFlow<List<PartEntity>> = _parts

    fun loadPartsForCar(carId: Int) {
        viewModelScope.launch {
            partRepository.getPartsForCar(carId).collect { partList ->
                _parts.value = partList
            }
        }
    }

    fun addPart(part: PartEntity) {
        viewModelScope.launch {
            partRepository.insertPart(part)
            loadPartsForCar(part.carId)
        }
    }

    fun updatePart(part: PartEntity) {
        viewModelScope.launch {
            partRepository.updatePart(part)
            loadPartsForCar(part.carId)
        }
    }

    fun deletePart(partId: Int, carId: Int) {
        viewModelScope.launch {
            partRepository.deletePart(partId)
            loadPartsForCar(carId)
        }
    }

    fun getPartById(partId: Int): Flow<PartEntity?> {
        return partRepository.getPartById(partId)
    }
}