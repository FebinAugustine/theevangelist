package com.febin.features.auth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.febin.features.auth.ui.components.FormButton
import com.febin.features.auth.ui.components.FormInput
import com.febin.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupClicked: (fullName: String, fellowshipName: String, phoneNo: String, email: String, password: String, userRole: String) -> Unit,
    onNavigateToSignin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var fellowshipName by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }

    var fellowshipExpanded by remember { mutableStateOf(false) }
    val fellowshipOptions = listOf("Main Fellowship", "Youth Fellowship", "Childrens Ministry") // Example options

    var roleExpanded by remember { mutableStateOf(false) }
    val roleOptions = listOf("USER", "ADMIN")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(0.dp), // Light gray-100 like background

        contentAlignment = Alignment.BottomCenter
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
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    placeholder = "Enter your full name",
                    leadingIcon = Icons.Filled.Person, // Changed
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = fellowshipExpanded,
                    onExpandedChange = { fellowshipExpanded = !fellowshipExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FormInput(
                        value = fellowshipName,
                        onValueChange = { fellowshipName = it }, // Allow typing or selection
                        label = "Fellowship Name",
                        placeholder = "Select or type fellowship",
                        leadingIcon = Icons.Filled.Group, // Changed
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fellowshipExpanded) },
                        readOnly = false, // Set to true if only selection is allowed
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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
                                    fellowshipName = selectionOption
                                    fellowshipExpanded = false
                                }
                            )
                        }
                    }
                }

                FormInput(
                    value = phoneNo,
                    onValueChange = { phoneNo = it },
                    label = "Phone Number",
                    placeholder = "Enter your phone number",
                    leadingIcon = Icons.Filled.Phone, // Changed
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                FormInput(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Filled.Email, // Changed
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                FormInput(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Create a password",
                    leadingIcon = Icons.Filled.Lock, // Changed
                    isPasswordToggleEnabled = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = roleExpanded,
                    onExpandedChange = { roleExpanded = !roleExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FormInput(
                        value = userRole,
                        onValueChange = {}, // Value is set by selection
                        label = "User Role",
                        placeholder = "Select your role",
                        leadingIcon = Icons.Filled.AccountCircle, // Changed
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                        readOnly = true, // Important for dropdown behavior with FormInput
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
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
                                    userRole = selectionOption
                                    roleExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FormButton(
                    text = "Sign Up",
                    onClick = { onSignupClicked(fullName, fellowshipName, phoneNo, email, password, userRole) },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = onNavigateToSignin) {
                    Text("Already have an account? Sign In")
                    Spacer(modifier = Modifier.height(32.dp))
                }
                Spacer(modifier = Modifier.height(42.dp))
            }
        }
    }
}
