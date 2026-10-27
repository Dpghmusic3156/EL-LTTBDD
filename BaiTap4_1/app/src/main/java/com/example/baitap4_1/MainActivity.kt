package com.example.baitap4_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.baitap4_1.ui.theme.BaiTap4_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap4_1Theme(darkTheme = false, dynamicColor = false) {
                val repository = remember { LibraryRepository() }
                LibraryApp(repository = repository)
            }
        }
    }
}
