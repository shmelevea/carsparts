package com.example.carsparts.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.data.ExportDataUseCase
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.utils.FILE_SAVED_SUCCESS
import com.example.carsparts.utils.FILE_SAVE_ERROR
import com.example.carsparts.utils.saveCarWithPartsToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val exportDataUseCase: ExportDataUseCase
) : ViewModel() {

    private val _cars = MutableStateFlow<List<CarEntity>>(emptyList())
    val cars: StateFlow<List<CarEntity>> = _cars

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _carToEdit = MutableStateFlow<CarEntity?>(null)
    val carToEdit: StateFlow<CarEntity?> = _carToEdit

    private val _saveResult = MutableStateFlow<String?>(null)
    val saveResult: StateFlow<String?> = _saveResult

    init {
        loadCars()
    }

    suspend fun isVinUnique(vin: String): Boolean {
        return carRepository.getCarByVin(vin) == null
    }

    private fun loadCars() {
        viewModelScope.launch {
            carRepository.getAllCars().collect { carList ->
                _cars.value = carList
            }
        }
    }

    fun addCar(car: CarEntity) {
        viewModelScope.launch {
            carRepository.insertCar(car)
            loadCars()
        }
    }

    fun onEditCar(car: CarEntity) {
        viewModelScope.launch {
            carRepository.updateCar(car)
            loadCars()
        }
    }

    fun onDeleteCar(car: CarEntity) {
        viewModelScope.launch {
            carRepository.deleteCar(car.id)
            loadCars()
        }
    }

    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }

    fun setCarToEdit(car: CarEntity?) {
        _carToEdit.value = car
    }

    fun saveCar(context: Context, carId: Int) {
        viewModelScope.launch {
            val car = _cars.value.find { it.id == carId }
            val vin = car?.vin ?: "unknown_vin"
            val file = saveCarWithPartsToFile(context, carId, vin, exportDataUseCase)
            _saveResult.value = if (file != null) FILE_SAVED_SUCCESS else FILE_SAVE_ERROR
        }
    }

    fun resetSaveResult() {
        _saveResult.value = null
    }
}