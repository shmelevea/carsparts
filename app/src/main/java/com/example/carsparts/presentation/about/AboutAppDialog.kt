package com.example.carsparts.presentation.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carsparts.R

@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.about_app))
        },
        text = {
            Column {
                Text(stringResource(R.string.app_info))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.contact_info))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.app_version))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.privacy_policy))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.terms_of_service))
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutAppDialog() {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AboutAppDialog(onDismiss = { openDialog.value = false })
    }
}
