package com.example.appnotas.nota

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appnotas.NotasTopAppBar
import com.example.appnotas.R
import com.example.appnotas.navigation.NavigationDestination
import kotlinx.coroutines.launch

object NotaDetailsDestination : NavigationDestination{
    override val route = "nota_details"
    override val titleRes = R.string.nota_detail_title

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotaDetailsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: NotaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            NotasTopAppBar(title = stringResource(NotaDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
                )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditNota(0) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_Nota_title))
            }
        }, modifier = modifier

    ) { innerPadding ->
        NotasDetailsBody(
            notaDetailsUiState = viewModel.notaUiState,
            onNotaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveNota()
                    navigateBack
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()

        )

    }
}
@Composable
fun NotaEntryBody(
    notaUiState : NotaUiState,
    onNotaValueChange : (NotaDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        NotaInputForm(
            notaDetails = notaUiState.notaDetails,
            onValueChange = onNotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = notaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(stringResource(R.string.save_action))
        }
    }

}
@Composable
fun NotaInputForm(
    notaDetails: NotaDetails,
    modifier: Modifier= Modifier,
    onValueChange: (NotaDetails) -> Unit = {},
    enabled : Boolean = true

){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))

    ) {
        outlined

    }

}
