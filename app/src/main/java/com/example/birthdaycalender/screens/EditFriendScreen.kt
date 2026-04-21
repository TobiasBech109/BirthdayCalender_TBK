package com.example.birthdaycalender.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.birthdaycalender.components.SimpleTopAppBar
import com.example.birthdaycalender.data.Friend

@Composable
fun EditFriendScreen(
    friend: Friend,
    onSave: (Friend) -> Unit,
    onCancel: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Edit", onLogout = onLogout)
        }
    ) { innerPadding ->
        FriendFormContent(
            modifier = Modifier.padding(innerPadding),
            initialFriend = friend,
            onSave = onSave,
            onCancel = onCancel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditFriendScreenPreview() {
    EditFriendScreen(
        friend = Friend(id = 1, name = "John Doe", birthDayOfMonth = 15, birthMonth = 8, birthYear = 1990, age = 34),
        onSave = {},
        onCancel = {},
        onLogout = {}
    )
}
