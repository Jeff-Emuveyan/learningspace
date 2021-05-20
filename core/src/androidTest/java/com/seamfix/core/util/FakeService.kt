package com.seamfix.core.util

import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.response.UserResponse
import com.seamfix.core.source.remote.Service
import retrofit2.Response

/***
 * This is just a FAKE class used for testing purposes only.
 * There is no need to provide implementations for it methods.
 * ***/
class FakeService: Service {
    override suspend fun fetchUsers(): Response<UserResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUser(userId: String): Response<UserDto> {
        TODO("Not yet implemented")
    }
}