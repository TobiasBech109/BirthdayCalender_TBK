package com.example.birthdaycalender.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birthdaycalender.data.Friend
import com.example.birthdaycalender.data.FriendsRepository
import com.example.birthdaycalender.data.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

data class FriendsUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val friends: List<Friend> = emptyList()
)

data class SingleFriendUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val friend: Friend? = null
)

class FriendsViewModel(
    private val friendsRepository: FriendsRepository // dependency injection
) : ViewModel() {

    private val _friendsUIState = MutableStateFlow(FriendsUIState())
    val friendsUIState: StateFlow<FriendsUIState> = _friendsUIState

    private val _singleFriendUIState = MutableStateFlow(SingleFriendUIState())

    private var originalFriendList: List<Friend> = emptyList()

    fun getFriends(userId: String) {
        _friendsUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = friendsRepository.getFriends()) {
                is NetworkResult.Success -> {
                    val filteredFriends = result.data.filter { it.userId == userId }
                    originalFriendList = filteredFriends
                    _friendsUIState.update { ui ->
                        ui.copy(isLoading = false, friends = filteredFriends)
                    }
                }
                is NetworkResult.Error -> {
                    _friendsUIState.update { ui ->
                        ui.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }
    fun addFriend(friend: Friend) {
        _singleFriendUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = friendsRepository.addFriend(friend)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, friend = result.data) }
                    friend.userId?.let { getFriends(it) }
                }
                is NetworkResult.Error -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }
    fun deleteFriend(id: Int, userId: String) {
        _singleFriendUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = friendsRepository.deleteFriend(id)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, friend = result.data) }
                    getFriends(userId)
                }
                is NetworkResult.Error -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    fun updateFriend(id: Int, friend: Friend) {
        _singleFriendUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = friendsRepository.updateFriend(id, friend)) {
                is NetworkResult.Success -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, friend = result.data) }
                    friend.userId?.let { getFriends(it) }
                }
                is NetworkResult.Error -> {
                    _singleFriendUIState.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    fun filterByName(nameFragment: String) {
        if (nameFragment.isBlank()) {
            _friendsUIState.update { it.copy(friends = originalFriendList) }
        } else {
            _friendsUIState.update { ui ->
                ui.copy(
                    friends = originalFriendList.filter { p ->
                        p.name.contains(nameFragment, ignoreCase = true)
                    }
                )
            }
        }
    }

    fun filterByAge(minAge: Int, maxAge: Int) {
        _friendsUIState.update { ui ->
            ui.copy(
                friends = originalFriendList.filter { friend ->
                    val age = friend.age
                    if (age != null) {
                        age in minAge..maxAge
                    } else {
                        minAge == 0 && maxAge == 120
                    }
                }
            )
        }
    }

    fun sortByName(ascending: Boolean) {
        _friendsUIState.update { ui ->
            ui.copy(
                friends = if (ascending)
                    ui.friends.sortedBy { it.name }
                else
                    ui.friends.sortedByDescending { it.name }
            )
        }
    }

    fun sortByAge(ascending: Boolean) {
        _friendsUIState.update { ui ->
            ui.copy(
                friends = if (ascending)
                    ui.friends.sortedWith(compareBy(nullsLast()) { it.age })
                else
                    ui.friends.sortedWith(compareBy(nullsLast<Int>()) { it.age }).reversed()
            )
        }
    }

    fun sortByBirthday(ascending: Boolean) {
        _friendsUIState.update { ui ->
            ui.copy(
                friends = if (ascending)
                    ui.friends.sortedWith(
                        compareBy(
                            { it.birthYear },
                            { it.birthMonth },
                            { it.birthDayOfMonth }
                        )
                    )
                else
                    ui.friends.sortedWith(
                        compareBy(
                            { it.birthYear },
                            { it.birthMonth },
                            { it.birthDayOfMonth }
                        )
                    ).reversed()
            )
        }
    }
}
