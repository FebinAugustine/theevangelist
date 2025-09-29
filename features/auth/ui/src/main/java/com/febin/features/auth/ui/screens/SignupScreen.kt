package com.febin.features.auth.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.febin.features.auth.ui.components.FormButton
import com.febin.features.auth.ui.components.FormInput
import com.febin.core.ui.R
import com.febin.features.auth.ui.stateIntentEffect.SignupEffect
import com.febin.features.auth.ui.stateIntentEffect.SignupIntent
import com.febin.features.auth.ui.viewmodel.SignupViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: SignupViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var fellowshipExpanded by remember { mutableStateOf(false) }
    val fellowshipOptions = listOf("Main Fellowship", "Youth Fellowship", "Childrens Ministry") // Example options

    var roleExpanded by remember { mutableStateOf(false) }
    val roleOptions = listOf("USER", "ADMIN")

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is SignupEffect.ShowError -> {
                    // This will now show API errors or general errors from ViewModel
                    // Validation errors are displayed directly below input fields
                    Log.d("SignupScreen", "Toast error: ${it.message}")
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    snackbarHostState.showSnackbar(it.message)
                }
                SignupEffect.NavigateToLogin -> {
                    val successMessage = "Registration Successful"
                    Log.d("SignupScreen", "Toast success: $successMessage")
                    Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
                    onNavigateToLogin()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFE1E1E1) // Lighter gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it)
                .padding(0.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
           
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp) // Adjusted padding for scroll
                    .shadow(5.dp),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,  // No bottom radius
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface // White background for Card
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()), // Added scroll for multiple inputs
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Adjusted spacing
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FormInput(
                        value = state.fullname,
                        onValueChange = { viewModel.onIntent(SignupIntent.FullnameChanged(it)) },
                        label = "Full Name",
                        placeholder = "Enter your full name",
                        leadingIcon = Icons.Filled.Person,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.fullnameError // ADDED
                    )
                    FormInput(
                        value = state.email,
                        onValueChange = { viewModel.onIntent(SignupIntent.EmailChanged(it)) },
                        label = "Email",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.emailError // ADDED
                    )

                    FormInput(
                        value = state.password,
                        onValueChange = { viewModel.onIntent(SignupIntent.PasswordChanged(it)) },
                        label = "Password",
                        placeholder = "Create a password",
                        leadingIcon = Icons.Filled.Lock,
                        isPasswordToggleEnabled = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.passwordError // ADDED
                    )

                    FormInput(
                        value = state.phone,
                        onValueChange = { viewModel.onIntent(SignupIntent.PhoneChanged(it)) },
                        label = "Phone Number",
                        placeholder = "Enter your phone number",
                        leadingIcon = Icons.Filled.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = state.phoneError // ADDED
                    )


                    ExposedDropdownMenuBox(
                        expanded = fellowshipExpanded,
                        onExpandedChange = { fellowshipExpanded = !fellowshipExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FormInput(
                            value = state.fellowship,
                            onValueChange = { viewModel.onIntent(SignupIntent.FellowshipChanged(it)) },
                            label = "Fellowship Name",
                            placeholder = "Select or type fellowship",
                            leadingIcon = Icons.Filled.Group,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fellowshipExpanded) },
                            readOnly = false, 
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            errorMessage = state.fellowshipError // ADDED
                        )
                        ExposedDropdownMenu(
                            expanded = fellowshipExpanded,
                            onDismissRequest = { fellowshipExpanded = false }
                        ) {
                            fellowshipOptions.forEach {
                                    selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        viewModel.onIntent(SignupIntent.FellowshipChanged(selectionOption))
                                        fellowshipExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = roleExpanded,
                        onExpandedChange = { roleExpanded = !roleExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FormInput(
                            value = state.role,
                            onValueChange = { viewModel.onIntent(SignupIntent.RoleChanged(it)) },
                            label = "User Role",
                            placeholder = "Select your role",
                            leadingIcon = Icons.Filled.AccountCircle,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                            readOnly = true, 
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            errorMessage = state.roleError // ADDED
                        )
                        ExposedDropdownMenu(
                            expanded = roleExpanded,
                            onDismissRequest = { roleExpanded = false }
                        ) {
                            roleOptions.forEach {
                                    selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        viewModel.onIntent(SignupIntent.RoleChanged(selectionOption))
                                        roleExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FormButton(
                        text = "Sign Up",
                        onClick = { viewModel.onIntent(SignupIntent.SignupClicked) },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Sign Up", fontSize=20.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("Already have an account? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF00008B))) {
                                append("Sign In")
                            }
                        },
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                }
            }
        }

    }
}
