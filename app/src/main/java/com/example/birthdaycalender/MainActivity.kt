package com.example.birthdaycalender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.birthdaycalender.data.Friend
import com.example.birthdaycalender.screens.EditFriendScreen
import com.example.birthdaycalender.screens.HomeScreen
import com.example.birthdaycalender.screens.LoginScreen
import com.example.birthdaycalender.screens.NewFriendScreen
import com.example.birthdaycalender.ui.theme.BirthdayCalenderTheme
import com.example.birthdaycalender.viewmodel.AuthenticationViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirthdayCalenderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthenticationViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("home") }
    // Track which friend we are currently editing
    var editingFriend by remember { mutableStateOf<Friend?>(null) }
    
    var friends by remember {
        mutableStateOf(
            listOf(
                Friend(1, name = "John Doe", birthDayOfMonth = 12, birthMonth = 5, birthYear = 1990, age = 34),
                Friend(2, name = "Jane Smith", birthDayOfMonth = 20, birthMonth = 11, birthYear = 1995, age = 28),
                Friend(3, name = "Bob Johnson", birthDayOfMonth = 5, birthMonth = 1, birthYear = 2000, age = 24)
            )
        )
    }
    var nextId by remember { mutableIntStateOf(4) }

    if (authViewModel.user == null) {
        LoginScreen(
            viewModel = authViewModel,
            onLoginSuccess = { 
                currentScreen = "home" 
            }
        )
    } else {
        when (currentScreen) {
            "home" -> HomeScreen(
                friends = friends,
                onAdd = { currentScreen = "new" },
                onEdit = { friend -> 
                    editingFriend = friend
                    currentScreen = "edit"
                },
                onDelete = { id ->
                    friends = friends.filter { it.id != id }
                },
                onLogout = { authViewModel.signOut() }
            )
            "new" -> NewFriendScreen(
                onSave = { newFriend ->
                    friends = friends + newFriend.copy(id = nextId++)
                    currentScreen = "home"
                },
                onCancel = { currentScreen = "home" },
                onLogout = { authViewModel.signOut() }
            )
            "edit" -> editingFriend?.let { friend ->
                EditFriendScreen(
                    friend = friend,
                    onSave = { updatedFriend ->
                        friends = friends.map { if (it.id == updatedFriend.id) updatedFriend else it }
                        currentScreen = "home"
                        editingFriend = null
                    },
                    onCancel = { 
                        currentScreen = "home"
                        editingFriend = null
                    },
                    onLogout = { authViewModel.signOut() }
                )
            }
        }
    }
}
