package com.waseefakhtar.doseapp.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.home.dialogs.DeleteDialog
import com.waseefakhtar.doseapp.feature.home.viewmodel.HomeState
import com.waseefakhtar.doseapp.feature.home.viewmodel.HomeViewModel
import com.waseefakhtar.doseapp.util.getTimeRemaining

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state
    HomeScreen(state, viewModel)
}

@Composable
fun HomeScreen(state: HomeState, viewModel: HomeViewModel) {

    AnimatedVisibility(
        visible = state.isDeleteDialogShown,
        enter = slideInVertically(
            initialOffsetY = {
                100
            },
            animationSpec = tween(300)
        ),
        exit = slideOutVertically  (
            animationSpec = tween(0),
            targetOffsetY = {
                -100
            }
        ),
    ) {
        DeleteDialog(
            medication = state.medicationToDelete,
            onDelete = {
                viewModel.deleteMedication(state.medicationToDelete)
            },
            onDismiss = {
                viewModel.closeDialog()
            })
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Greeting()
        DailyOverview(state)
        DailyMedications(
            state = state,
            viewModel = viewModel,
            showDeleteDialog = {
                viewModel.showDialog(it)
            }
        )
    }
}

@Composable
fun Greeting() {
    Column {
        // TODO: Add greeting based on time of day e.g. Good Morning, Good Afternoon, Good evening.
        // TODO: Get name from DB and show user's first name.
        Text(
            text = "Good morning,",
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "Kathryn!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun DailyOverview(state: HomeState) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
    ) {

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.36F),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Your plan for today",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "${state.medications.filter { it.medicationTaken }.size} of ${state.medications.size} completed",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun DailyMedications(
    state: HomeState,
    viewModel: HomeViewModel,
    showDeleteDialog : (medication : Medication) -> Unit
) {

    Text(
        modifier = Modifier
            .padding(4.dp, 12.dp, 8.dp, 0.dp)
            .fillMaxWidth(),
        text = "Today".uppercase(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = state.medications.sortedBy { it.medicationTaken },
            itemContent = {
                MedicationCard(
                    medication = it,
                    viewModel = viewModel,
                    showDeleteDialog = showDeleteDialog
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicationCard(
    showDeleteDialog: (medication : Medication) -> Unit,
    medication: Medication,
    viewModel: HomeViewModel,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(30.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Box(
            Modifier
                .pointerInput(Unit){
                    detectTapGestures(
                        onLongPress = {
                            showDeleteDialog(medication)
                        }
                    )
                }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = medication.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = medication.timesOfDay.joinToString(", ")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = getTimeRemaining(medication),
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    Button(
                        onClick = {
                            viewModel.takeMedication(medication)
                        },
                        enabled = !medication.medicationTaken
                    ) {
                        if (medication.medicationTaken) {
                            Text(
                                text = "Taken"
                            )
                        } else {
                            Text(
                                text = "Take now"
                            )
                        }
                    }
                }
            }
        }


    }
}
