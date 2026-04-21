package com.example.birthdaycalender.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.birthdaycalender.viewmodel.AuthenticationViewModel

@Composable
fun LoginScreen(
    viewModel: AuthenticationViewModel,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(viewModel.user) {
        if (viewModel.user != null) {
            onLoginSuccess()
        }
    }

    LoginContent(
        message = viewModel.message,
        onLogin = { email, password -> viewModel.signIn(email, password) },
        onRegister = { email, password -> viewModel.register(email, password) }
    )
}

@Composable
fun LoginContent(
    message: String = "",
    onLogin: (String, String) -> Unit = { _, _ -> },
    onRegister: (String, String) -> Unit = { _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegistering) "Register" else "Login",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isRegistering) {
                    onRegister(email, password)
                } else {
                    onLogin(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isRegistering) "Register" else "Login")
        }

        TextButton(
            onClick = { isRegistering = !isRegistering }
        ) {
            Text(
                text = if (isRegistering) 
                    "Already have an account? Login" 
                else 
                    "Don't have an account? Register"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent()
}
