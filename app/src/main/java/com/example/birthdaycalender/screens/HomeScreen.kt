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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onEdit: (Friend) -> Unit,
    onDelete: (Int) -> Unit,
    onLogout: () -> Unit,
    navigateToLogin: () -> Unit = {},
    sortByName: (Boolean) -> Unit = {},
    sortByAge: (Boolean) -> Unit = {},
    sortByBirthday: (Boolean) -> Unit = {},
    filterByName: (String) -> Unit = {},
    filterByAge: (Int, Int) -> Unit = { _, _ -> }
) {
    var nameAsc by remember { mutableStateOf(true) }
    var ageAsc by remember { mutableStateOf(true) }
    var birthdayAsc by remember { mutableStateOf(true) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

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
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    TextButton(
                        onClick = {
                            nameAsc = !nameAsc
                            sortByName(nameAsc)
                        },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("Name ${if (nameAsc) "↑" else "↓"}", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(
                        onClick = {
                            ageAsc = !ageAsc
                            sortByAge(ageAsc)
                        },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("Age ${if (ageAsc) "↑" else "↓"}", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(
                        onClick = {
                            birthdayAsc = !birthdayAsc
                            sortByBirthday(birthdayAsc)
                        },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text("Date ${if (birthdayAsc) "↑" else "↓"}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Button(onClick = { showFilterSheet = true }) {
                    Text("Filter")
                }
            }

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

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState
            ) {
                FilterContent(
                    onFilterByName = filterByName,
                    onFilterByAge = filterByAge,
                    onClose = { showFilterSheet = false }
                )
            }
        }
    }
}

@Composable
fun FilterContent(
    onFilterByName: (String) -> Unit,
    onFilterByAge: (Int, Int) -> Unit,
    onClose: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var minAge by remember { mutableStateOf("") }
    var maxAge by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filter Friends", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onFilterByName(it)
            },
            label = { Text("Search by name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { 
                        searchText = ""
                        onFilterByName("")
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = minAge,
                onValueChange = { minAge = it },
                label = { Text("Min Age") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxAge,
                onValueChange = { maxAge = it },
                label = { Text("Max Age") },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                val min = minAge.toIntOrNull() ?: 0
                val max = maxAge.toIntOrNull() ?: 120
                onFilterByAge(min, max)
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply Age Filter")
        }

        TextButton(
            onClick = {
                searchText = ""
                minAge = ""
                maxAge = ""
                onFilterByName("")
                onFilterByAge(0, 120)
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear All Filters")
        }
    }
}

@Composable
fun FriendItem(
    friend: Friend,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
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
