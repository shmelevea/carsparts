package com.example.carsparts.presentation.parts

import android.widget.Toast
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carsparts.R
import com.example.carsparts.domain.entity.PartEntity

@Composable
fun PartItem(
    part: PartEntity,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val borderColor = MaterialTheme.colorScheme.outline


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
                .clickable { onClick(part.id) }
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
                IconButton(onClick = { onDelete(part.id) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(R.string.remove_spare_part),
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
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = part.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = stringResource(R.string.quantity_km, part.mileageKm),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                    )

                    Text(
                        text = part.partNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clickable {
                                clipboardManager.setText(AnnotatedString(part.partNumber))
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.part_number_copied_to_clipboard),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()

                            }
                            .padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
                    )
                }

                IconButton(onClick = { onEdit(part.id) },
                    modifier = Modifier.size(30.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_create),
                        contentDescription = stringResource(R.string.edit_part),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPartItem() {
    PartItem(
        part = PartEntity(
            id = 1,
            carId = 1,
            partNumber = "123456789",
            secondPartNumber = "223456789",
            name = "Масляный фильтр",
            purchaseDate = "10.01.2024",
            replacementDate = "10.06.2024",
            replacementCost = 5000,
            partCost = 1500,
            store = "АвтоМаг",
            mileageKm = 55666,
            breakdownMileageKm = 55666,
            description = "Масляный фильтр для двигателя"
        ),
        onClick = {},
        onDelete = {},
        onEdit = {}
    )
}