package com.example.birthdaylist.data

interface FriendsRepository {
    suspend fun getFriends(): NetworkResult<List<Friend>>
    suspend fun addFriend(friend: Friend): NetworkResult<Friend>
    suspend fun deleteFriend(id: Int): NetworkResult<Friend>
    suspend fun updateFriend(id: Int, data: Friend): NetworkResult<Friend>
}