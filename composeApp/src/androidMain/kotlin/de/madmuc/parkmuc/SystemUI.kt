package de.madmuc.parkmuc

import android.app.Activity
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun HideSystemBars() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val activity = context as? Activity
        activity?.window?.insetsController?.hide(WindowInsets.Type.systemBars())
        activity?.window?.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
    }
}