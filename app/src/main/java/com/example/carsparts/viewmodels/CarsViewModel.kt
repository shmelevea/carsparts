package com.example.carsparts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _cars = MutableStateFlow<List<CarEntity>>(emptyList())
    val cars: StateFlow<List<CarEntity>> = _cars

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _carToEdit = MutableStateFlow<CarEntity?>(null)
    val carToEdit: StateFlow<CarEntity?> = _carToEdit

    init {
        loadCars()
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
}