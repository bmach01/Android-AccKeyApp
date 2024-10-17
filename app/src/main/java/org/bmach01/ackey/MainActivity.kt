package org.bmach01.ackey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.bmach01.ackey.domain.BiometricHelper
import org.bmach01.ackey.ui.theme.AcKeyTheme
import org.bmach01.ackey.ui.view.AcKeyApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BiometricHelper.prepare(this)


        setContent {
            AcKeyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AcKeyApp()
                }
            }
        }
    }
}
