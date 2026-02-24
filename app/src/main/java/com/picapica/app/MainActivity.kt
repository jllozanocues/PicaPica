package com.picapica.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.picapica.app.data.AppDatabase
import com.picapica.app.ui.screens.HistoryView
import com.picapica.app.ui.screens.MainScreen
import com.picapica.app.viewmodel.PicaPicaViewModel
import com.picapica.app.viewmodel.PicaPicaViewModelFactory

import com.picapica.app.ui.theme.PicaPicaTheme
import android.view.WindowManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Premium Glassmorphism: Blur behind and 20% Dimming
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            window.attributes.blurBehindRadius = 40 // Set blur intensity
            window.setDimAmount(0.2f) // 20% darkening
        }

        val database = AppDatabase.getDatabase(this)
        val viewModelFactory = PicaPicaViewModelFactory(database.shiftDao())

        setContent {
            PicaPicaTheme {
                val viewModel: PicaPicaViewModel = viewModel(factory = viewModelFactory)
                val weeklyHistory by viewModel.weeklyHistory.collectAsState()
                var showHistory by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Main Screen
                    MainScreen(
                        viewModel = viewModel,
                        onOpenHistory = { showHistory = true }
                    )

                    // History View with Smooth Popup Zoom transition
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showHistory,
                        enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)) + 
                                androidx.compose.animation.scaleIn(initialScale = 0.9f, animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)),
                        exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)) + 
                               androidx.compose.animation.scaleOut(targetScale = 0.9f, animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing))
                    ) {
                        HistoryView(
                            onBack = { showHistory = false },
                            history = weeklyHistory
                        )
                    }
                }
            }
        }
    }
}

