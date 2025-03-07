package com.example.carsparts.presentation.parts

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.viewmodels.PartsViewModel

@Composable
fun PartsListScreen(
    carId: Int,
    name: String,
    brand: String,
    model: String,
    viewModel: PartsViewModel = hiltViewModel(),
    onPartSelected: (PartEntity) -> Unit,
    onAddPart: () -> Unit,
    onEditPart: (PartEntity) -> Unit
) {
    val partsList by viewModel.parts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredPartsList = partsList
        .filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true) ||
                    it.partNumber.contains(searchQuery, ignoreCase = true)
        }
        .sortedWith(
            compareBy<PartEntity> { it.replacementDate.isEmpty() }
                .thenByDescending { it.replacementDate }
                .thenBy { it.name }
        )

    val groupedParts = filteredPartsList
        .groupBy { it.replacementDate.ifEmpty { "" } }
        .toSortedMap(compareByDescending { it })

    Scaffold { paddingValues ->
        PartsListScreenContent(
            carId = carId,
            name = name,
            brand = brand,
            model = model,
            groupedParts = groupedParts,
            onEditPart = onEditPart,
            onDeletePart = { part -> viewModel.deletePart(part.id, part.carId) },
            onLoadParts = { viewModel.loadPartsForCar(carId) },
            onPartSelected = onPartSelected,
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it },
            onAddPart = onAddPart,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun PartsListScreenContent(
    carId: Int,
    name: String,
    brand: String,
    model: String,
    groupedParts: Map<String, List<PartEntity>>,
    onEditPart: (PartEntity) -> Unit,
    onDeletePart: (PartEntity) -> Unit,
    onLoadParts: () -> Unit,
    onPartSelected: (PartEntity) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onAddPart: () -> Unit,
    modifier: Modifier = Modifier
) {

    var partToEdit by remember { mutableStateOf<PartEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var partToDelete by remember { mutableStateOf<PartEntity?>(null) }

    LaunchedEffect(carId) { onLoadParts() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "$name, $brand $model",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                groupedParts.forEach { (date, parts) ->
                    item {
                        val displayDate = date.ifEmpty {
                            stringResource(id = R.string.replacement_date_not_provided)
                        }
                        Text(
                            text = displayDate,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    items(parts) { part ->
                        PartItem(
                            part = part,
                            onClick = { onPartSelected(part) },
                            onDelete = {
                                partToDelete = part
                                showDeleteDialog = true
                            },
                            onEdit = {
                                partToEdit = part
                                onEditPart(part)
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onAddPart,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_part),
            )
        }
    }

    if (showDeleteDialog && partToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_part)) },
            text = {
                Text(
                    stringResource(
                        R.string.are_you_sure_you_want_to_delete_the_part,
                        partToDelete!!.name
                    )
                )
            },
            confirmButton = {
                Button(onClick = {
                    partToDelete?.let(onDeletePart)
                    showDeleteDialog = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPartsListScreen() {
    val fakeParts = listOf(
        PartEntity(
            id = 1,
            carId = 1,
            partNumber = "123456789",
            secondPartNumber = "223456789",
            name = "Мсляный фильтр",
            purchaseDate = "10.01.2024",
            replacementDate = "10.06.2024",
            replacementCost = 5000,
            partCost = 15000,
            store = "АвтоМаг",
            mileageKm = 55666,
            breakdownMileageKm = 55666,
            description = "Масляный фильтр для двигателя"
        ),
        PartEntity(
            id = 1,
            carId = 1,
            partNumber = "123456789",
            secondPartNumber = "223456789",
            name = "Масляный фильтр2",
            purchaseDate = "10.01.2024",
            replacementDate = "10.06.2024",
            replacementCost = 5000,
            partCost = 15000,
            store = "АвтоМаг",
            mileageKm = 55666,
            breakdownMileageKm = 55666,
            description = "Масляный фильтр для двигателя"
        ),
        PartEntity(
            id = 2,
            carId = 1,
            partNumber = "987654321",
            secondPartNumber = "887654321",
            name = "Воздушный фильтр",
            purchaseDate = "15.02.2024",
            replacementDate = "15.07.2024",
            replacementCost = 7000,
            partCost = 20000,
            store = "АвтоДеталь",
            mileageKm = 60000,
            breakdownMileageKm = 60000,
            description = "Воздушный фильтр для системы охлаждения"
        ),
        PartEntity(
            id = 3,
            carId = 1,
            partNumber = "111222333",
            secondPartNumber = "444555666",
            name = "Тормозные колодки",
            purchaseDate = "01.03.2024",
            replacementDate = "",
            replacementCost = 3000,
            partCost = 9000,
            store = "АвтоМаг",
            mileageKm = 55000,
            breakdownMileageKm = 54000,
            description = "Тормозные колодки для тормозной системы"
        ),
        PartEntity(
            id = 3,
            carId = 1,
            partNumber = "111222333",
            secondPartNumber = "444555666",
            name = "Тормозные колодки",
            purchaseDate = "01.03.2024",
            replacementDate = "",
            replacementCost = 3000,
            partCost = 9000,
            store = "АвтоМаг",
            mileageKm = 55000,
            breakdownMileageKm = 54000,
            description = "Тормозные колодки для тормозной системы"
        )
    )

    val groupedParts = fakeParts
        .sortedWith(compareByDescending<PartEntity> { it.replacementDate }.thenBy { it.name })
        .groupBy { it.replacementDate }

    PartsListScreenContent(
        carId = 1,
        name = "My",
        brand = "Toyota",
        model = "Camry",
        groupedParts = groupedParts,
        onEditPart = {},
        onDeletePart = {},
        onLoadParts = {},
        onPartSelected = {},
        searchQuery = "",
        onAddPart = {},
        onSearchQueryChanged = {}
    )
}