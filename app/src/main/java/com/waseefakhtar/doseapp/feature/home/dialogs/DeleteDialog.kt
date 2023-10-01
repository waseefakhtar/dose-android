package com.waseefakhtar.doseapp.feature.home.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication

@Composable
fun DeleteDialog(
    medication : Medication?,
    onDelete : () -> Unit,
    onDismiss : () -> Unit
) {
    Dialog(
        onDismissRequest = {
        onDismiss()
    },
        properties = DialogProperties(
        usePlatformDefaultWidth = false
      )
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(5.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.delete_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.delete_warning, medication?.name ?: ""),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    Button(onClick = {
                        onDelete()
                    },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete_title),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                    OutlinedButton(onClick = {
                        onDismiss()
                    }
                    ){
                        Text(text = stringResource(id = R.string.close_dialog))
                    }

                }
            }

        }

    }
}