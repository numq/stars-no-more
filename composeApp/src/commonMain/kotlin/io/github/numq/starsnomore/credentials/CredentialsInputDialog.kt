package io.github.numq.starsnomore.credentials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CredentialsInputDialog(
    credentials: Credentials,
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
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Token") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                visualTransformation = PasswordVisualTransformation(),
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