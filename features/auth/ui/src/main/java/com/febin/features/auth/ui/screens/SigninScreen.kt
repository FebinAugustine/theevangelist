package com.febin.features.auth.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
    onNavigateToUserDashboard: (userId: String) -> Unit,
    onNavigateToAdminDashboard: (userId: String) -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
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
                is SigninEffect.NavigateToUserDashboard -> {
                    Timber.tag("SigninScreen").d("Navigating to User Dashboard. User ID: ${it.userId}")
                    Toast.makeText(context, "Sign-in Successful!", Toast.LENGTH_SHORT).show()
                    onNavigateToUserDashboard(it.userId)
                }
                is SigninEffect.NavigateToAdminDashboard -> {
                    Timber.tag("SigninScreen").d("Navigating to Admin Dashboard. User ID: ${it.userId}")
                    Toast.makeText(context, "Sign-in Successful! Welcome Admin!", Toast.LENGTH_SHORT).show()
                    onNavigateToAdminDashboard(it.userId)
                }
                is SigninEffect.NavigateToForgotPassword -> {
                    Timber.tag("SigninScreen").d("Navigating to Forgot Password")
                    onNavigateToForgotPassword()
                }
                is SigninEffect.NavigateToSignup -> {
                    Timber.tag("SigninScreen").d("Navigating to Signup from effect")
                    onNavigateToSignup()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.theevangelist_logo),
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(180.dp),
        )
        Text(
            text = "The Evangelist",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )

        ) {
            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
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
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Filled.Lock,
                    isPasswordToggleEnabled = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = state.passwordError
                )

                // Add Forgot Password text button here if desired
                // TextButton(onClick = { viewModel.onIntent( ??? ) }) { Text("Forgot Password?") }

                Spacer(modifier = Modifier.height(16.dp))

                FormButton(
                    text = "Sign In",
                    onClick = { viewModel.onIntent(SigninIntent.SigninClicked) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Sign In", fontSize=20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Don't have an account? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) { // Use theme color
                            append("Sign Up")
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToSignup() }
                )
                Spacer(modifier = Modifier.height(42.dp))
            }
        }
    }
}
