package com.example.birthdaycalender.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FriendsRepositoryImpl(
    private val friendsAPI: FriendsAPI,
    private val dispatcher: CoroutineDispatcher
) : FriendsRepository {
    override suspend fun getFriends(): NetworkResult<List<Friend>> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.getFriends()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error("Response body is null")
                    }
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun addFriend(friend: Friend): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.addFriend(friend)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null)
                        NetworkResult.Success(body)
                    else
                        NetworkResult.Error("Response body is null")
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    override suspend fun deleteFriend(id: Int): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.deleteFriend(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null)
                        NetworkResult.Success(body)
                    else
                        NetworkResult.Error("Response body is null")
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }


    override suspend fun updateFriend(id: Int, data: Friend): NetworkResult<Friend> {
        return withContext(dispatcher) {
            try {
                val response = friendsAPI.updateFriend(id, data)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error("Response body is null")
                    }
                } else
                    NetworkResult.Error(response.message())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}