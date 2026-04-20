package com.example.birthdaycalender

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.birthdaycalender.data.Friend
import com.example.birthdaycalender.screens.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenNameTest() {
        val friend1 = Friend(id = 1, name = "Patrick", birthYear = 1998, birthMonth = 3)
        val friend2 = Friend(id = 2, name = "Valdemar", birthYear = 1997, birthMonth = 7)
        val myFriends = listOf(friend1, friend2)

        composeTestRule.setContent {
            HomeScreen(
                friends = myFriends,
                onAdd = {},
                onEdit = {},
                onDelete = {},
                onLogout = {},
                navigateToLogin = {}
            )
        }

        composeTestRule.onNodeWithText("Patrick").assertIsDisplayed()
        composeTestRule.onNodeWithText("Valdemar").assertIsDisplayed()
    }
}