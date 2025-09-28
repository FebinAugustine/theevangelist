package com.febin.features.auth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box // Added import
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush // Added import
import androidx.compose.ui.graphics.Color // Already present, but ensure it is
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.febin.core.ui.R
import com.febin.features.auth.ui.components.FormButton
import com.febin.features.auth.ui.components.FormInput

@Composable
fun SigninScreen(
    onSigninClicked: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val shadowColor = MaterialTheme.colorScheme.onSurfaceVariant // Base color for custom shadow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(0.dp), // Overall padding for the screen content if any, can be 0.dp
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Image(
            painter = painterResource(id = R.drawable.theevangelist_logo),
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(220.dp)
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "The Evangelist",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // This Box is lifted and contains both the fake shadow and the Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp) // Original padding for the card group
                .graphicsLayer {
                    translationY = -25f // This lifts the entire Box (shadow + card)
                }
        ) {
            // 1. Fake Shadow Box (drawn first, so it's behind the Card content)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp) //  <<--- SHORTER HEIGHT for a more concentrated shadow glow
                    .align(Alignment.TopCenter) // Position shadow at the very top of the parent Box
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                shadowColor.copy(alpha = 0.15f), // <<--- ADJUSTED ALPHA for a softer glow
                                Color.Transparent
                            )
                        ),
                        // Match the Card's top corners for the shadow shape
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
            )

            // 2. Actual Card (drawn on top of the fake shadow)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp) // <<--- ADDED PADDING to push card slightly down, revealing shadow above
                ,
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Important: Turn off default card shadow
            ) {
                // Card content starts here
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    FormInput(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    FormInput(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "Enter your password",
                        leadingIcon = Icons.Filled.Lock,
                        isPasswordToggleEnabled = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FormButton(
                        text = "Sign In",
                        onClick = { onSigninClicked(email, password) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextButton(onClick = onNavigateToSignup) {
                        Text("Don't have an account? Sign Up")
                    }
                    Spacer(modifier = Modifier.height(42.dp))
                }
            }
        }
    }
}
