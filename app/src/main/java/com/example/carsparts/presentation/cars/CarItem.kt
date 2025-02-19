package com.example.carsparts.presentation.cars

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carsparts.R
import com.example.carsparts.domain.entity.CarEntity

@Composable
fun CarItem(
    car: CarEntity,
    onClick: (CarEntity) -> Unit,
    onEdit: (CarEntity) -> Unit,
    onDelete: (CarEntity) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    val borderColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable { onClick(car) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = car.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${car.brand} ${car.model}, ${car.year}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = car.vin,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clickable {
                            clipboardManager.setText(AnnotatedString(car.vin))
                            Toast
                                .makeText(context,
                                    context.getString(R.string.vin_copied_to_clipboard), Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }
            IconButton(onClick = { onEdit(car) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_create),
                    contentDescription = "Edit car",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete car",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удаление машины") },
            text = { Text("Вы действительно хотите удалить машину \"${car.name}\"?") },
            confirmButton = {
                Button(onClick = {
                    onDelete(car)
                    showDeleteDialog = false
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
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
        onClick = { /* Нажатие обработано */ },
        onEdit = { /* Редактирование обработано */ },
        onDelete = { /* Удаление обработано */ }
    )
}