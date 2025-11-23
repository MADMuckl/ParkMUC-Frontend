package de.madmuc.parkmuc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: SettingsViewModel
) {
    var notificationsEnabled by remember {
        mutableStateOf(viewModel.getSetting("notifications") as? Boolean ?: true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = { onNavigate(Screen.MapScreen) },
                modifier = Modifier.padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1b98d5)
                )
            ) {
                Text("← Zurück", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Einstellungen",
                fontSize = 28.sp,
                color = Color(0xFF005a9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                notificationsEnabled = !notificationsEnabled
                viewModel.saveSetting("notifications", notificationsEnabled)
            }) {
                Text("Benachrichtigungen: ${if (notificationsEnabled) "An" else "Aus"}")
            }
        }
    }
}