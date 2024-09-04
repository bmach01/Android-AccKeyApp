package org.bmach01.ackey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.bmach01.ackey.data.LocalSettings
import org.bmach01.ackey.ui.theme.AcKeyTheme
import org.bmach01.ackey.ui.view.MainKeyView
import org.bmach01.ackey.ui.view.MainRegisterView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = LocalSettings(context = baseContext)

//        val t = BiometricHelper(this)
//        t.test()

        setContent {
            AcKeyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    MainKeyView()
                    MainRegisterView()
//                    MainSettingsView()
//                    MainPasswordKeyboardView()
                }
            }
        }
    }
}
