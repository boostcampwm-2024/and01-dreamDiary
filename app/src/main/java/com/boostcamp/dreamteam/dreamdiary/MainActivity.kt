package com.boostcamp.dreamteam.dreamdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.boostcamp.dreamteam.dreamdiary.feature.diary.DiaryHomeScreen
import com.boostcamp.dreamteam.dreamdiary.ui.DreamDiaryApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //DreamDiaryApp()
            DiaryHomeScreen()
        }
    }
}
