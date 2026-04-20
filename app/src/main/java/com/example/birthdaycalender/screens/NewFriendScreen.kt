package com.example.birthdaycalender.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.birthdaycalender.components.SimpleTopAppBar
import com.example.birthdaycalender.data.Friend
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewFriendScreen(
    onSave: (Friend) -> Unit,
    onCancel: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "New", onLogout = onLogout)
        }
    ) { innerPadding ->
        FriendFormContent(
            modifier = Modifier.padding(innerPadding),
            onSave = onSave,
            onCancel = onCancel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendFormContent(
    modifier: Modifier = Modifier,
    initialFriend: Friend? = null,
    onSave: (Friend) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(initialFriend?.name ?: "") }

    // Safe formatting to prevent crashes if values are null
    val initialDateStr = if (initialFriend != null) {
        val d = initialFriend.birthDayOfMonth ?: 0
        val m = initialFriend.birthMonth ?: 0
        val y = initialFriend.birthYear ?: 0
        if (d > 0) "%02d.%02d.%d".format(d, m, y) else ""
    } else ""

    var birthdayStr by remember { mutableStateOf(initialDateStr) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        birthdayStr = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(text = "Name", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter name...") }
            )
        }

        Column {
            Text(text = "Birthday", style = MaterialTheme.typography.titleLarge)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = birthdayStr,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Select birthday...") },
                    readOnly = true,
                    enabled = true,
                    trailingIcon = {
                        TextButton(onClick = { showDatePicker = true }) { Text("Select") }
                    }
                )
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val parts = birthdayStr.split(".")
                    val d = parts.getOrNull(0)?.toIntOrNull()
                    val m = parts.getOrNull(1)?.toIntOrNull()
                    val y = parts.getOrNull(2)?.toIntOrNull()
                    val calculatedAge = y?.let { Calendar.getInstance().get(Calendar.YEAR) - it }

                    val friend = Friend(
                        id = initialFriend?.id ?: 0,
                        userId = initialFriend?.userId,
                        name = name,
                        birthDayOfMonth = d,
                        birthMonth = m,
                        birthYear = y,
                        age = calculatedAge
                    )
                    onSave(friend)
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank()
            ) { Text("Save") }

            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
        }
    }
}
