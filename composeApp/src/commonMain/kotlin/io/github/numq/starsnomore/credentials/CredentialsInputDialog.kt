package io.github.numq.starsnomore.credentials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CredentialsInputDialog(
    credentials: Credentials,
    isTokenVisible: Boolean,
    toggleTokenVisibility: () -> Unit,
    onDismissRequest: () -> Unit,
    onCredentialsSubmit: (Credentials) -> Unit,
) {
    var name by remember { mutableStateOf(credentials.name) }

    var token by remember { mutableStateOf(credentials.token) }

    val isFormValid by remember { derivedStateOf { name.isNotBlank() && token.isNotBlank() } }

    AlertDialog(
        onDismissRequest = onDismissRequest, title = {
            Text(
                text = "Enter credentials", style = MaterialTheme.typography.headlineSmall
            )
        }, text = {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Enter your credentials or edit the credentials.json file",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Name") },
                    placeholder = {
                        when {
                            credentials.name.isBlank() -> Text("Enter your name")

                            else -> Text("Current: ${credentials.name}")
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    trailingIcon = {
                        IconButton(onClick = { name = "" }, enabled = name.isNotBlank()) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    })

                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Token") },
                    placeholder = {
                        when {
                            credentials.token.isBlank() -> Text("Enter your token")

                            else -> Text("Current: ${credentials.token}")
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    visualTransformation = if (isTokenVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 4.dp, alignment = Alignment.CenterHorizontally
                            ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = toggleTokenVisibility) {
                                when {
                                    isTokenVisible -> Icon(Icons.Default.VisibilityOff, null)

                                    else -> Icon(Icons.Default.Visibility, null)
                                }
                            }
                            IconButton(onClick = { token = "" }, enabled = token.isNotBlank()) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear token")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        }, confirmButton = {
            Button(
                onClick = { onCredentialsSubmit(Credentials(name = name, token = token)) },
                enabled = isFormValid,
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Update")
            }
        }, dismissButton = {
            TextButton(
                onClick = onDismissRequest, shape = MaterialTheme.shapes.medium
            ) {
                Text("Cancel")
            }
        }, shape = MaterialTheme.shapes.large
    )
}