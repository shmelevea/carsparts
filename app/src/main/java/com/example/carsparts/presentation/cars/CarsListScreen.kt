package com.example.carsparts.presentation.cars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.presentation.addCar.AddCarDialog
import com.example.carsparts.viewmodels.CarsViewModel

@Composable
fun CarsListScreen(
    onCarSelected: (CarEntity) -> Unit,
    viewModel: CarsViewModel = hiltViewModel()
) {
    val carList by viewModel.cars.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val carToEdit by viewModel.carToEdit.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val sortedCarList = carList
        .filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.brand.contains(searchQuery, ignoreCase = true) ||
                    it.model.contains(searchQuery, ignoreCase = true)
        }
        .sortedWith(compareBy({ it.name }, { it.brand }, { it.model }))

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.setCarToEdit(null)
                    viewModel.setShowDialog(true)
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_car),
                )
            }
        }
    ) { paddingValues ->
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
            modifier = Modifier.padding(paddingValues)
        )
    }
    AddCarDialog(
        showDialog = showDialog,
        onDismiss = { viewModel.setShowDialog(false) },
        onAddCar = { car ->
            viewModel.addCar(car)
            viewModel.setShowDialog(false)
        },
        carToEdit = carToEdit,
        onEditCar = { car ->
            viewModel.onEditCar(car)
            viewModel.setShowDialog(false)
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.garage),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
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
                        onDelete = { onDeleteCar(car) }
                    )
                }
            }
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
        onSearchQueryChanged = {}
    )
}