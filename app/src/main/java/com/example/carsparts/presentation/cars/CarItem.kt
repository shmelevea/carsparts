package com.example.carsparts.presentation.cars

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.carsparts.R
import com.example.carsparts.domain.entity.CarEntity

@Composable
fun CarItem(
    car: CarEntity,
    onClick: (CarEntity) -> Unit,
    onEdit: (CarEntity) -> Unit,
    onDelete: (CarEntity) -> Unit,
    onSave: (CarEntity) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    val borderColor = MaterialTheme.colorScheme.outline

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onSave(car)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_required_to_save),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick(car) }
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f),
                    RoundedCornerShape(8.dp),
                )
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
            ) {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(R.string.delete_car),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                    )
                    Text(
                        text = "${car.brand} ${car.model}, ${car.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                    )
                    Text(
                        text = car.vin,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clickable {
                                clipboardManager.setText(AnnotatedString(car.vin))
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.vin_copied_to_clipboard),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                            .padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                    )
                }

                IconButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                onSave(car)
                            } else {
                                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        } else {
                            onSave(car)
                        }
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(R.string.save_car),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { onEdit(car) },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = stringResource(R.string.edit_car),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.delete_car)) },
                text = { Text(stringResource(R.string.want_to_delete_car, car.name)) },
                confirmButton = {
                    Button(onClick = {
                        onDelete(car)
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
}

@Preview(showBackground = true)
@Composable
fun PreviewCarItem() {
    CarItem(
        car = CarEntity(
            id = 1,
            name = "My Car",
            brand = "Tesla",
            model = "Model S",
            year = 2021,
            vin = "5YJSA1E26MF123456"
        ),
        onClick = {},
        onEdit = {},
        onDelete = {},
        onSave = {}
    )
}