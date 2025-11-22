package de.madmuc.parkmuc

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // ComposeViewport is the current recommended way to start Compose on web. :contentReference[oaicite:6]{index=6}
    ComposeViewport(document.body!!) {
        App()   // <- shared UI again
    }
}
