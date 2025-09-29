package com.febin.features.auth.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febin.core.ui.R
import com.febin.features.auth.ui.components.FormButton
import com.febin.features.auth.ui.components.FormInput
import com.febin.features.auth.ui.stateIntentEffect.SigninEffect
import com.febin.features.auth.ui.stateIntentEffect.SigninIntent
import com.febin.features.auth.ui.viewmodel.SigninViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SigninScreen(
    viewModel: SigninViewModel = koinViewModel(),
    onNavigateToUserDashboard: (userId: String) -> Unit, // MODIFIED
    onNavigateToAdminDashboard: (userId: String) -> Unit, // MODIFIED
    onNavigateToSignup: () -> Unit, // MODIFIED
    onNavigateToForgotPassword: () -> Unit // ADDED for completeness
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val shadowColor = MaterialTheme.colorScheme.onSurfaceVariant

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is SigninEffect.ShowError -> {
                    Timber.tag("SigninScreen").d("Toast error: ${it.message}")
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    snackbarHostState.showSnackbar(it.message)
                }
                is SigninEffect.NavigateToUserDashboard -> { // MODIFIED
                    Timber.tag("SigninScreen").d("Navigating to User Dashboard. User ID: ${it.userId}")
                    Toast.makeText(context, "Sign-in Successful!", Toast.LENGTH_SHORT).show() // Optional success toast
                    onNavigateToUserDashboard(it.userId)
                }
                is SigninEffect.NavigateToAdminDashboard -> { // MODIFIED
                    Timber.tag("SigninScreen").d("Navigating to Admin Dashboard. User ID: ${it.userId}")
                    Toast.makeText(context, "Sign-in Successful! Welcome Admin!", Toast.LENGTH_SHORT).show() // Optional success toast
                    onNavigateToAdminDashboard(it.userId)
                }
                is SigninEffect.NavigateToForgotPassword -> {
                    Timber.tag("SigninScreen").d("Navigating to Forgot Password")
                    onNavigateToForgotPassword()
                }
                is SigninEffect.NavigateToSignup -> { // This handles if ViewModel sends this effect for some reason
                    Timber.tag("SigninScreen").d("Navigating to Signup from effect")
                    onNavigateToSignup()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(0.dp),
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp)
                .graphicsLayer {
                    translationY = -25f
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                shadowColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
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
                        value = state.email,
                        onValueChange = { viewModel.onIntent(SigninIntent.EmailChanged(it)) },
                        label = "Email",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.emailError
                    )

                    FormInput(
                        value = state.password,
                        onValueChange = { viewModel.onIntent(SigninIntent.PasswordChanged(it)) },
                        label = "Password",
                        placeholder = "Enter your password", // MODIFIED PLACEHOLDER
                        leadingIcon = Icons.Filled.Lock,
                        isPasswordToggleEnabled = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), // MODIFIED IME ACTION
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.passwordError
                    )

                    // Add Forgot Password text button here if desired
                    // TextButton(onClick = { viewModel.onIntent( ??? ) }) { Text("Forgot Password?") }

                    Spacer(modifier = Modifier.height(16.dp))

                    FormButton(
                        text = "Sign In", // MODIFIED BUTTON TEXT
                        onClick = { viewModel.onIntent(SigninIntent.SigninClicked) },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Sign In", fontSize=20.sp) // MODIFIED BUTTON TEXT
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account? ") // MODIFIED TEXT
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) { // Use theme color
                                append("Sign Up") // MODIFIED TEXT
                            }
                        },
                        modifier = Modifier.clickable { onNavigateToSignup() } // MODIFIED ACTION
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                }
            }
        }
    }
}
