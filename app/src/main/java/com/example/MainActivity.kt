package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.storage.LearningRepository
import com.example.ui.MainApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = LearningRepository.getInstance(applicationContext)

        setContent {
            val themeMode by repository.themeMode.collectAsState()

            MyApplicationTheme(themeMode = themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp(repository = repository)
                }
            }
        }
    }
}
