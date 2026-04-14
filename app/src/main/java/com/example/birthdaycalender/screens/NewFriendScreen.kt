package com.example.birthdaycalender.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.birthdaycalender.components.SimpleTopAppBar
import com.example.birthdaycalender.data.Friend
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

/**
 * This is the shared UI for both New and Edit screens.
 * By putting it here, we avoid creating a new file while still reusing code.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendFormContent(
    modifier: Modifier = Modifier,
    initialFriend: Friend? = null,
    onSave: (Friend) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(initialFriend?.name ?: "") }
    
    // Format the date for the text field if we have an initial friend
    val initialDateStr = if (initialFriend?.birthDayOfMonth != null && 
        initialFriend.birthMonth != null && 
        initialFriend.birthYear != null) {
        "%02d.%02d.%d".format(initialFriend.birthDayOfMonth, initialFriend.birthMonth, initialFriend.birthYear)
    } else {
        ""
    }
    
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
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                        TextButton(onClick = { showDatePicker = true }) {
                            Text("Select")
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
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
                    if (parts.size == 3) {
                        val d = parts[0].toIntOrNull()
                        val m = parts[1].toIntOrNull()
                        val y = parts[2].toIntOrNull()
                        val calculatedAge = if (y != null) Calendar.getInstance().get(Calendar.YEAR) - y else null
                        
                        val friend = (initialFriend ?: Friend(name = name)).copy(
                            name = name,
                            birthDayOfMonth = d,
                            birthMonth = m,
                            birthYear = y,
                            age = calculatedAge
                        )
                        onSave(friend)
                    } else {
                        // Handle incomplete date or just save with name
                        onSave((initialFriend ?: Friend(name = name)).copy(name = name))
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }
    }
}
