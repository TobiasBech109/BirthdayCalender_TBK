package com.example.birthdaycalender.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.birthdaycalender.data.Friend
import com.example.birthdaycalender.components.SimpleTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    friends: List<Friend>,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onEdit: (Friend) -> Unit, // Changed to pass the whole Friend object
    onDelete: (Int) -> Unit,
    onLogout: () -> Unit,
    navigateToLogin: () -> Unit = {},
    sortByName: (Boolean) -> Unit = {},
    sortByAge: (Boolean) -> Unit = {},
    sortByBirthday: (Boolean) -> Unit = {},
    filterByName: (String) -> Unit = {},
    filterByAge: (Int, Int) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Home", onLogout = onLogout)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add Friend")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Sorting and Filter Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    TextButton(onClick = { sortByName(true) }, modifier = Modifier.padding(horizontal = 2.dp)) {
                        Text("Name ↑↓", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(onClick = { sortByAge(true) }, modifier = Modifier.padding(horizontal = 2.dp)) {
                        Text("Year ↑↓", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(onClick = { sortByBirthday(true) }, modifier = Modifier.padding(horizontal = 2.dp)) {
                        Text("Date ↑↓", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Button(onClick = { /* TODO: Open Filter */ }) {
                    Text("Filter")
                }
            }

            // Friends List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(friends) { friend ->
                    FriendItem(
                        friend = friend,
                        onClick = { onEdit(friend) },
                        onDelete = { onDelete(friend.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    friend: Friend,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    // Format the birthday for display
    val birthdayDisplay = if (friend.birthDayOfMonth != null && friend.birthMonth != null) {
        "%02d.%02d.%d".format(friend.birthDayOfMonth, friend.birthMonth, friend.birthYear ?: 0)
    } else {
        "Unknown"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = friend.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${friend.age ?: 0} y.o.",
                modifier = Modifier.weight(0.7f)
            )
            Text(
                text = birthdayDisplay,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Friend")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleFriends = listOf(
        Friend(1, name = "John Doe", birthDayOfMonth = 12, birthMonth = 5, birthYear = 1990, age = 34),
        Friend(2, name = "Jane Smith", birthDayOfMonth = 20, birthMonth = 11, birthYear = 1995, age = 28)
    )
    HomeScreen(
        friends = sampleFriends,
        onAdd = {},
        onEdit = {},
        onDelete = {},
        onLogout = {}
    )
}
