package com.example.birthdaycalender


sealed class NavRoutes(val route: String) {
    data object FriendList : NavRoutes("list")
    data object EditFriend : NavRoutes("details")
    data object NewFriend : NavRoutes("add")
    data object Login : NavRoutes("login")
}