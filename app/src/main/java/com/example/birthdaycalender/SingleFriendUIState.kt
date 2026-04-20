package com.example.birthdaycalender

import com.example.birthdaycalender.data.Friend

data class SingleFriendUIState(
    val isLoading: Boolean = false,
    val friend: Friend? = null,
    val error: String? = null
)