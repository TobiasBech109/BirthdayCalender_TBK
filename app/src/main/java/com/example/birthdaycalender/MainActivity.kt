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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.birthdaycalender.data.Friend
import com.example.birthdaycalender.screens.EditFriendScreen
import com.example.birthdaycalender.screens.HomeScreen
import com.example.birthdaycalender.screens.LoginScreen
import com.example.birthdaycalender.screens.NewFriendScreen
import com.example.birthdaycalender.ui.theme.BirthdayCalenderTheme
import com.example.birthdaycalender.viewmodel.AuthenticationViewModel
import com.example.birthdaycalender.viewmodel.FriendsViewModel
import org.koin.androidx.compose.koinViewModel


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
                        AppNavigation(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthenticationViewModel = viewModel()
) {
    val navController = rememberNavController()
    val friendsViewModel: FriendsViewModel = koinViewModel()
    val friendsUIState by friendsViewModel.friendsUIState.collectAsStateWithLifecycle()
    val user = authViewModel.user

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(NavRoutes.Login.route) {
                popUpTo(0)
            }
        } else {
            friendsViewModel.getFriends(user.uid)
            navController.navigate(NavRoutes.FriendList.route) {
                popUpTo(0)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (user == null) NavRoutes.Login.route else NavRoutes.FriendList.route
    ) {
        composable(NavRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.FriendList.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.FriendList.route) {
            HomeScreen(
                friends = friendsUIState.friends,
                modifier = modifier,
                onAdd = { navController.navigate(NavRoutes.NewFriend.route) },
                onEdit = { friend ->
                    navController.navigate(NavRoutes.EditFriend.route + "/${friend.id}")
                },
                onDelete = { id ->
                    user?.let { friendsViewModel.deleteFriend(id, it.uid) }
                },
                onLogout = {
                    authViewModel.signOut()
                },
                sortByName = { asc -> friendsViewModel.sortByName(asc) },
                sortByAge = { asc -> friendsViewModel.sortByAge(asc) },
                sortByBirthday = { asc -> friendsViewModel.sortByBirthday(asc) },
                filterByName = { name -> friendsViewModel.filterByName(name) },
                filterByAge = { min, max -> friendsViewModel.filterByAge(min, max) }
            )
        }
        composable(
            NavRoutes.EditFriend.route + "/{friendId}",
            arguments = listOf(navArgument("friendId") { type = NavType.IntType })
        ) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getInt("friendId")
            val friend = friendsUIState.friends.find { it.id == friendId } ?: Friend(name = "Unknown")
            
            EditFriendScreen(
                friend = friend,
                onSave = { updatedFriend ->
                    user?.let { 
                        val friendToUpdate = updatedFriend.copy(userId = it.uid)
                        friendsViewModel.updateFriend(friendToUpdate.id, friendToUpdate) 
                    }
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                onLogout = {
                    authViewModel.signOut()
                }
            )
        }
        composable(NavRoutes.NewFriend.route) {
            NewFriendScreen(
                onSave = { newFriend ->
                    user?.let {
                        friendsViewModel.addFriend(newFriend.copy(userId = it.uid))
                    }
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                onLogout = {
                    authViewModel.signOut()
                }
            )
        }
    }
}
