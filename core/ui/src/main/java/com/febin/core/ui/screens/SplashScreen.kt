package com.febin.core.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.febin.core.ui.R // Assuming R is accessible from core:ui, or adjust package
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.theevangelist_logo), // Using background for now, ic_launcher.webp is hard to preview directly
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(200.dp) // Adjust size as needed
        )
    }

    LaunchedEffect(Unit) {
        delay(1000) // Simulating a 1-second splash screen, adjust as needed
        onTimeout()
    }
}