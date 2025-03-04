package com.example.carsparts.presentation.settings

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsparts.R
import com.example.carsparts.presentation.about.AboutAppDialog
import com.example.carsparts.viewmodels.ImportViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: ImportViewModel = hiltViewModel()
) {

    val importSuccess by viewModel.importSuccess.collectAsState()
    val context = LocalContext.current
    var showAboutAppDialog by rememberSaveable { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.use { stream ->
                    val json = stream.bufferedReader().readText()
                    viewModel.importJson(json)
                }
            }
        }
    )

    LaunchedEffect(importSuccess) {
        importSuccess?.let {
            val message = if (it) R.string.import_success else R.string.import_error
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearImportStatus()
        }
    }

    SettingsScreenContent(
        modifier = modifier,
        filePickerLauncher = filePickerLauncher,
        onAboutAppClick = { showAboutAppDialog = true }
    )

    if (showAboutAppDialog) {
        AboutAppDialog(onDismiss = { showAboutAppDialog = false })
    }
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    filePickerLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    onAboutAppClick: () -> Unit
) {
    val aboutApp = stringResource(R.string.about_app)
    val importData = stringResource(R.string.import_data)

    val settingsItems = listOf(aboutApp, importData)

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            LazyColumn {
                items(settingsItems) { item ->
                    SettingsItem(title = item, onClick = {
                        when (item) {
                            aboutApp -> {
                                onAboutAppClick()
                            }

                            importData -> {
                                filePickerLauncher.launch(arrayOf("application/json"))
                            }
                        }
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreenContent() {
    SettingsScreenContent(
        filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
            onResult = {}
        ),
        onAboutAppClick = {}
    )
}
