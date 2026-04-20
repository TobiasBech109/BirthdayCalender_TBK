package com.example.birthdaycalender

import com.example.birthdaycalender.data.Friend


data class FriendsUIState (
    val isLoading: Boolean = false,
    val books: List<Friend> = emptyList(),
    val error: String? = null
)