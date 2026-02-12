package com.example.carsparts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.entity.parsedReplacementDate
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

    private val _part = MutableStateFlow<PartEntity?>(null)
    val part: StateFlow<PartEntity?> = _part

    fun loadPartInfo(partId: Int) {
        viewModelScope.launch {
            partRepository.getPartById(partId).collect { partEntity ->
                _part.value = partEntity
            }
        }
    }

    fun loadPartsForCar(carId: Int) {
        viewModelScope.launch {
            partRepository.getPartsForCar(carId).collect { partList ->
                _parts.value = sortParts(partList)
            }
        }
    }

    private fun sortParts(parts: List<PartEntity>): List<PartEntity> =
        parts.sortedWith(
            compareBy<PartEntity> { it.parsedReplacementDate == null }
                .thenByDescending { it.parsedReplacementDate }
                .thenBy { it.name }
        )

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