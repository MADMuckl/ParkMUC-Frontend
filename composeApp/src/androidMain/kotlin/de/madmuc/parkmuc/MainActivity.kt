package de.madmuc.parkmuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        window.insetsController?.hide(android.view.WindowInsets.Type.systemBars())
//        window.insetsController?.systemBarsBehavior =
//            android.view.WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH


        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}