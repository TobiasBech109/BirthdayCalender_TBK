package com.example.birthdaycalender.components

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(title: String, onLogout: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            Button(onClick = onLogout) {
                Text("Logout")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleTopAppBarPreview() {
    SimpleTopAppBar(
        title = "Home",
        onLogout = { }
    )
}
