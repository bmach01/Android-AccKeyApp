package org.bmach01.ackey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.bmach01.ackey.ui.theme.AcKeyTheme
import org.bmach01.ackey.ui.views.MainKeyView
import org.bmach01.ackey.ui.views.MainPasswordKeyboardView
import org.bmach01.ackey.ui.views.MainRegisterView
import org.bmach01.ackey.ui.views.MainSettingsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcKeyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    MainKeyView()
//                    MainRegisterView()
//                    MainSettingsView()
                    MainPasswordKeyboardView()
                }
            }
        }
    }
}
