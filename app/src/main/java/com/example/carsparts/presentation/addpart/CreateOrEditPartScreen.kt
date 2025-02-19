package com.example.carsparts.presentation.addpart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.presentation.datePicker.DatePickerModal
import com.example.carsparts.viewmodels.PartsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateOrEditPartScreen(
    carId: Int,
    partToEdit: PartEntity? = null,
    onDismiss: () -> Unit,
    partsViewModel: PartsViewModel = hiltViewModel()
) {

    var part by rememberSaveable { mutableStateOf(partToEdit ?: PartEntity(
        carId = carId,
        partNumber = "",
        secondPartNumber = "",
        name = "",
        purchaseDate = "",
        replacementDate = "",
        replacementCost = 0,
        partCost = 0,
        store = "",
        mileageKm = 0,
        breakdownMileageKm = 0,
        description = ""
    )) }

    var isInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(partToEdit) {
        if (partToEdit != null && !isInitialized) {
            part = partToEdit
            isInitialized = true
        }
    }

    CreateOrEditPartScreenContent(
        part = part,
        onPartChange = { updatedPart -> part = updatedPart },
        onSave = {
            if (partToEdit != null) {
                partsViewModel.updatePart(part)
            } else {
                partsViewModel.addPart(part)
            }
            onDismiss()
        },
        onDismiss = onDismiss,
        isEditing = partToEdit != null
    )
}

@Composable
fun CreateOrEditPartScreenContent(
    part: PartEntity,
    onPartChange: (PartEntity) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    isEditing: Boolean
) {

    val scrollState = rememberScrollState()
    val titleResId = if (isEditing) R.string.edit_part else R.string.add_part

    var showPurchaseDatePicker by remember { mutableStateOf(false) }
    var showReplacementDatePicker by remember { mutableStateOf(false) }

    val isSaveEnabled = part.name.isNotBlank() && part.partNumber.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.name,
            onValueChange = {
                if (it.length <= 20) onPartChange(part.copy(name = it))
            },
            label = { Text(stringResource(R.string.part_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.partNumber,
            onValueChange = { onPartChange(part.copy(partNumber = it)) },
            label = { Text(stringResource(R.string.part_number)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.secondPartNumber,
            onValueChange = {
                if (it.length <= 20) onPartChange(part.copy(secondPartNumber = it))
            },
            label = { Text(stringResource(R.string.second_part_number)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.partCost.takeIf { it != 0 }?.toString()
                ?: "",
            onValueChange = {
                onPartChange(part.copy(partCost = it.toIntOrNull() ?: 0))
            },
            label = { Text(stringResource(R.string.part_cost)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.purchaseDate,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.purchase_date)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showPurchaseDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = stringResource(
                            R.string.select_date
                        )
                    )
                }
            }
        )

        if (showPurchaseDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                        onPartChange(part.copy(purchaseDate = formattedDate))
                    }
                    showPurchaseDatePicker = false
                },
                onDismiss = { showPurchaseDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.replacementCost.takeIf { it != 0 }?.toString()
                ?: "",
            onValueChange = {
                onPartChange(part.copy(replacementCost = it.toIntOrNull() ?: 0))
            },
            label = { Text(stringResource(R.string.replacement_cost)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.replacementDate,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.replacement_date)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showReplacementDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = stringResource(
                            R.string.select_date
                        )
                    )
                }
            }
        )

        if (showReplacementDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                        onPartChange(part.copy(replacementDate = formattedDate))
                    }
                    showReplacementDatePicker = false
                },
                onDismiss = { showReplacementDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.store,
            onValueChange = {
                if (it.length <= 20) {
                    onPartChange(part.copy(store = it))
                }
            },
            label = { Text(stringResource(R.string.store)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.mileageKm.takeIf { it != 0 }?.toString()
                ?: "",
            onValueChange = {
                onPartChange(part.copy(mileageKm = it.toIntOrNull() ?: 0))
            },
            label = { Text(stringResource(R.string.mileage_km)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.breakdownMileageKm.takeIf { it != 0 }?.toString()
                ?: "",
            onValueChange = {
                onPartChange(part.copy(breakdownMileageKm = it.toIntOrNull() ?: 0))
            },
            label = { Text(stringResource(R.string.breakdown_mileage_km)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = part.description,
            onValueChange = {
                if (it.length <= 300) onPartChange(part.copy(description = it))
            },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {

            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = isSaveEnabled
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateOrEditPartScreenContent() {
    val fakePart = PartEntity(
        carId = 1,
        partNumber = "12345",
        secondPartNumber = "54321",
        name = "Brake Pad",
        purchaseDate = "2024-01-15",
        replacementDate = "2025-01-15",
        replacementCost = 150,
        partCost = 120,
        store = "AutoParts Store",
        mileageKm = 50000,
        breakdownMileageKm = 30000,
        description = "High quality brake pad for safety"
    )

    CreateOrEditPartScreenContent(
        part = fakePart,
        onPartChange = {},
        onSave = {},
        onDismiss = {},
        isEditing = true
    )
}