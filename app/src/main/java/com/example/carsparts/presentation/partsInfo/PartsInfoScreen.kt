package com.example.carsparts.presentation.partsInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.viewmodels.PartsViewModel

@Composable
fun PartsInfoScreen(
    partId: Int,
    onEdit: (PartEntity) -> Unit,
    viewModel: PartsViewModel = hiltViewModel()
) {

    val part = viewModel.part.collectAsState(initial = null).value

    LaunchedEffect(partId) {
        viewModel.loadPartInfo(partId)
    }

    if (part != null) {
        PartsInfoScreenContent(
            part = part,
            onEdit = onEdit

        )
    }
}

@Composable
fun PartsInfoScreenContent(
    part: PartEntity,
    onEdit: (PartEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = part.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))

            InfoRow(label = stringResource(R.string.part_number), value = part.partNumber)

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.second_part_number),
                value = part.secondPartNumber
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(label = stringResource(R.string.purchase_date), value = part.purchaseDate)

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.part_cost),
                value = stringResource(R.string.quantity_rub, part.partCost)
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.replacement_date),
                value = part.replacementDate
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.replacement_cost),
                value = stringResource(R.string.quantity_rub, part.replacementCost)
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(label = stringResource(R.string.store), value = part.store)

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.mileage_km),
                value = stringResource(
                    R.string.quantity_km,
                    part.mileageKm
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                label = stringResource(R.string.breakdown_mileage_km),
                value = stringResource(
                    R.string.quantity_km,
                    part.breakdownMileageKm
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = part.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = Int.MAX_VALUE
                )
            }
        }

        FloatingActionButton(
            onClick = { onEdit(part) },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit_part)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPartsInfoScreenContent() {
    val fakePart = PartEntity(
        id = 1,
        carId = 1,
        partNumber = "123456789",
        secondPartNumber = "223456789",
        name = "Масляный фильтр",
        purchaseDate = "10.01.2024",
        replacementDate = "10.06.2024",
        replacementCost = 500,
        partCost = 15000,
        store = "АвтоМаг",
        mileageKm = 55666,
        breakdownMileageKm = 55666,
        description = "Фильтр масляный для автомобиля"
    )

    PartsInfoScreenContent(
        part = fakePart,
        onEdit = {}
    )
}
