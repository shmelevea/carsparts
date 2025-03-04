package com.example.carsparts.presentation.addCar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.utils.capitalizeFirstLetter
import com.example.carsparts.utils.formatVin
import com.example.carsparts.utils.sanitizeInput
import com.example.carsparts.viewmodels.CarsViewModel
import java.util.Calendar


@Composable
fun AddCarDialog(
    viewModel: CarsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val showDialog by viewModel.showDialog.collectAsState()
    val carToEdit by viewModel.carToEdit.collectAsState()

    if (showDialog) {
        AddCarDialogContent(
            carToEdit = carToEdit,
            onDismiss = onDismiss,
            onConfirm = { car ->
                if (carToEdit != null) {
                    viewModel.onEditCar(car)
                } else {
                    viewModel.addCar(car)
                }
                onDismiss()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarDialogContent(
    carToEdit: CarEntity?,
    onDismiss: () -> Unit,
    onConfirm: (CarEntity) -> Unit,
) {
    var carName by rememberSaveable { mutableStateOf(carToEdit?.name ?: "") }
    var carBrand by rememberSaveable { mutableStateOf(carToEdit?.brand ?: "") }
    var carModel by rememberSaveable { mutableStateOf(carToEdit?.model ?: "") }
    var carYear by rememberSaveable { mutableStateOf(carToEdit?.year?.toString() ?: "") }
    var carVin by rememberSaveable { mutableStateOf(carToEdit?.vin ?: "") }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1910..currentYear).toList().reversed()

    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                if (carToEdit != null) stringResource(R.string.edit_car)
                else stringResource(R.string.add_car)
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = carName,
                        onValueChange = {
                            carName = capitalizeFirstLetter(sanitizeInput(it))
                        },
                        label = { Text(stringResource(R.string.name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = carBrand,
                        onValueChange = {
                            carBrand = capitalizeFirstLetter(sanitizeInput(it))
                        },
                        label = { Text(stringResource(R.string.brand)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = carModel,
                        onValueChange = {
                            carModel = capitalizeFirstLetter(sanitizeInput(it))
                        },
                        label = { Text(stringResource(R.string.model)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = carVin,
                        onValueChange = {
                            carVin = sanitizeInput(it, maxLength = 17, transform = ::formatVin)
                        },
                        label = { Text(stringResource(R.string.vin)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = carYear,
                            onValueChange = {},
                            label = { Text(stringResource(R.string.year)) },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = stringResource(R.string.purchase_date)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            years.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year.toString()) },
                                    onClick = {
                                        carYear = year.toString()
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val car = CarEntity(
                        id = carToEdit?.id ?: 0,
                        name = carName,
                        brand = carBrand,
                        model = carModel,
                        year = carYear.toIntOrNull(),
                        vin = carVin
                    )
                    onConfirm(car)
                },
                enabled = carName.isNotEmpty() &&
                        carBrand.isNotEmpty() &&
                        carModel.isNotEmpty() &&
                        carYear.isNotEmpty() &&
                        carVin.isNotEmpty()
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddCarDialogContent() {
    val carToEdit = CarEntity(
        id = 1,
        name = "My Car",
        brand = "Toyota",
        model = "Corolla",
        year = 2020,
        vin = "12345678901234567"
    )

    AddCarDialogContent(
        carToEdit = carToEdit,
        onDismiss = {},
        onConfirm = {}
    )
}