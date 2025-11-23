package de.madmuc.parkmuc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpecialRightsScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: SettingsViewModel
) {
    val isWoman = remember {
        mutableStateOf(viewModel.getSetting("isWoman") as? Boolean ?: false)
    }
    val hasECar = remember {
        mutableStateOf(viewModel.getSetting("hasECar") as? Boolean ?: false)
    }
    val hasDisabilityPass = remember {
        mutableStateOf(viewModel.getSetting("hasDisabilityPass") as? Boolean ?: false)
    }
    val hasResidentPass = remember {
        mutableStateOf(viewModel.getSetting("hasResidentPass") as? Boolean ?: false)
    }
    val residentPassLocation = remember {
        mutableStateOf(viewModel.getSetting("residentPassLocation") as? String ?: "")
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
                .verticalScroll(rememberScrollState())
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
                "Sonderpark Rechte",
                fontSize = 28.sp,
                color = Color(0xFF005a9f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Checkbox(
                    checked = isWoman.value,
                    onCheckedChange = {
                        isWoman.value = it
                        viewModel.saveSetting("isWoman", it)
                    }
                )
                Text(
                    "Ich bin eine Frau",
                    modifier = Modifier.padding(start = 12.dp, top = 16.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Checkbox(
                    checked = hasECar.value,
                    onCheckedChange = {
                        hasECar.value = it
                        viewModel.saveSetting("hasECar", it)
                    }
                )
                Text(
                    "Ich habe ein E-Auto",
                    modifier = Modifier.padding(start = 12.dp, top = 16.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Checkbox(
                    checked = hasDisabilityPass.value,
                    onCheckedChange = {
                        hasDisabilityPass.value = it
                        viewModel.saveSetting("hasDisabilityPass", it)
                    }
                )
                Text(
                    "Ich habe einen Behindertenausweis",
                    modifier = Modifier.padding(start = 12.dp, top = 16.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Checkbox(
                    checked = hasResidentPass.value,
                    onCheckedChange = {
                        hasResidentPass.value = it
                        viewModel.saveSetting("hasResidentPass", it)
                    }
                )
                Text(
                    "Ich habe einen Anwohnerausweis",
                    modifier = Modifier.padding(start = 12.dp, top = 16.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (hasResidentPass.value) {
                Text(
                    "Wo ist dein Anwohnerausweis gültig?",
                    fontSize = 14.sp,
                    color = Color(0xFF005a9f),
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = residentPassLocation.value,
                    onValueChange = {
                        residentPassLocation.value = it
                        viewModel.saveSetting("residentPassLocation", it)
                    },
                    placeholder = { Text("z.B. Parkhaus ABC, Bereich 3") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            Button(
                onClick = {
                    // Data is already saved on each change, just navigate back
                    onNavigate(Screen.MapScreen)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1b98d5)
                )
            ) {
                Text("Speichern", color = Color.White)
            }
        }
    }
}