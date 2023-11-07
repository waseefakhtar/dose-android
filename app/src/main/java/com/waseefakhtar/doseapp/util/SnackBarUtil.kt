package com.waseefakhtar.doseapp.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.waseefakhtar.doseapp.ui.theme.Pink40
import kotlinx.coroutines.launch

class SnackBarUtil {

    companion object {
        private val snackbarMsgOneShot = mutableStateOf("")
        private var snackbarVisibility = mutableStateOf(false)

        fun showSnackbar(message: String) {
            snackbarMsgOneShot.value = message
            snackbarVisibility.value = true
        }

        fun getSnackbarMsg() = snackbarMsgOneShot

        fun hideSnackbar() {
            snackbarVisibility.value = false
        }

        fun getSnackbarVisibility() = snackbarVisibility

        @Composable
        fun SnackbarWithoutScaffold(
            message: String,
            isVisible: Boolean,
            onVisibilityChange: (Boolean) -> Unit
        ) {
            val snackState = remember { SnackbarHostState() }
            val snackScope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(bottom = 76.dp)
                    .zIndex(10f),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(
                    modifier = Modifier,
                    hostState = snackState
                ) {
                    Snackbar(
                        snackbarData = it,
                        containerColor = Pink40,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                }

            }

            if (isVisible) {
                LaunchedEffect(Unit) {
                    snackScope.launch {
                        snackState.showSnackbar(message)
                        onVisibilityChange(false)
                    }
                }
            }
        }

    }

}
