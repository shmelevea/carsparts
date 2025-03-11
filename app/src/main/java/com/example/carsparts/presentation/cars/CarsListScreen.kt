package com.example.carsparts.presentation.cars

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.presentation.addCar.AddCarDialog
import com.example.carsparts.utils.FILE_SAVED_SUCCESS
import com.example.carsparts.utils.FILE_SAVE_ERROR
import com.example.carsparts.viewmodels.CarsViewModel

@Composable
fun CarsListScreen(
    onCarSelected: (CarEntity) -> Unit,
    viewModel: CarsViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val carList by viewModel.cars.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val saveResult by viewModel.saveResult.collectAsState()

    val successMessage = stringResource(id = R.string.file_saved_success)
    val errorMessage = stringResource(id = R.string.file_save_error)

    LaunchedEffect(saveResult) {
        if (saveResult != null) {
            val message = when (saveResult) {
                FILE_SAVED_SUCCESS -> successMessage
                FILE_SAVE_ERROR -> errorMessage
                else -> ""
            }
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.resetSaveResult()
            }
        }
    }

    val sortedCarList = carList
        .filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.brand.contains(searchQuery, ignoreCase = true) ||
                    it.model.contains(searchQuery, ignoreCase = true)
        }
        .sortedWith(compareBy({ it.name }, { it.brand }, { it.model }))

    Scaffold { paddingValues ->
        CarsListScreenContent(
            carList = sortedCarList,
            onCarSelected = onCarSelected,
            onEditCar = {
                viewModel.setCarToEdit(it)
                viewModel.setShowDialog(true)
            },
            onDeleteCar = viewModel::onDeleteCar,
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it },
            onSaveCar = { carId -> viewModel.saveCar(context, carId) },
            onAddCarClick = {
                viewModel.setCarToEdit(null)
                viewModel.setShowDialog(true)
            },
            modifier = Modifier.padding(paddingValues),
            onSettingsClick = onSettingsClick
        )
    }

    AddCarDialog(
        viewModel = viewModel,
        onDismiss = {
            viewModel.setShowDialog(false)
            viewModel.setCarToEdit(null)
        }
    )
}

@Composable
fun CarsListScreenContent(
    carList: List<CarEntity>,
    onCarSelected: (CarEntity) -> Unit,
    onEditCar: (CarEntity) -> Unit,
    onDeleteCar: (CarEntity) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSaveCar: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onAddCarClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.garage),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text(stringResource(R.string.search)) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.clear_search),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(carList) { car ->
                    CarItem(
                        car = car,
                        onClick = { onCarSelected(car) },
                        onEdit = { onEditCar(car) },
                        onDelete = { onDeleteCar(car) },
                        onSave = { onSaveCar(car.id) }
                    )
                }
            }
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(R.string.settings)
            )
        }

        FloatingActionButton(
            onClick = onAddCarClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_car),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCarsListScreen() {
    val fakeCars = listOf(
        CarEntity(
            id = 1,
            name = "My",
            brand = "Toyota",
            model = "Camry",
            year = 2022,
            vin = "JT123456789012345",

            ),
        CarEntity(
            id = 2,
            name = "Mom",
            brand = "Honda",
            model = "Civic",
            year = 2020,
            vin = "2HGFA165X7H012345"
        )
    )
    CarsListScreenContent(
        carList = fakeCars,
        onCarSelected = {},
        onEditCar = {},
        onDeleteCar = {},
        searchQuery = "",
        onSaveCar = {},
        onSearchQueryChanged = {},
        onAddCarClick = {},
        onSettingsClick = {}
    )
}