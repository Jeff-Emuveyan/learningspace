package com.seamfix.core.repositories

import com.nhaarman.mockitokotlin2.mock
import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.response.UserResponse
import com.seamfix.core.source.local.AppDatabase
import com.seamfix.core.source.remote.Service
import com.seamfix.core.util.FakeResponseBody
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response

const val ID = "aDummyId"

class UserRepositoryTest{

    private var service: Service = mock()
    private var database: AppDatabase  = mock()

    @Test
    fun `If network response code is 200, a user should be returned when getUserFromRemoteSource(ID) is called`() =
        runBlocking {
            val user = UserDto(id = ID, firstName = "JEFF")

            Mockito.`when`(service.fetchUser(ID)).thenReturn(Response.success(user))
            val repo = UserRepository(service, database)

            val userResponse = repo.getUserFromRemoteSource(ID)
            assertThat(userResponse?.id, `is`(equalTo(user.id)))
        }


    @Test
    fun `If network response code is NOT 200, null should be returned when getUserFromRemoteSource(ID) is called`() =
        runBlocking {

            Mockito.`when`(service.fetchUser(ID)).thenReturn(Response.error(400, FakeResponseBody()))
            val repo = UserRepository(service, database)

            val userResponse = repo.getUserFromRemoteSource(ID)
            assertThat(userResponse, `is`(equalTo(null)))
        }


    @Test
    fun `If network response code is 200, a list of users should be returned when getUsersFromRemoteSource() is called`() =
        runBlocking {

            val userA = UserDto(id = ID, firstName = "JEFF")
            val userB = UserDto(id = ID, firstName = "JAMES")
            val userC = UserDto(id = ID, firstName = "JOY")

            val userResponse = UserResponse()
            userResponse.data = arrayOf(userA, userB, userC)

            Mockito.`when`(service.fetchUsers()).thenReturn(Response.success(userResponse))
            val repo = UserRepository(service, database)

            val result = repo.getUsersFromRemoteSource()
            assertTrue(result != null && result.size == 3)
        }


    @Test
    fun `If network response code is NOT 200, null should be returned when getUsersFromRemoteSource() is called`() =
        runBlocking {

            //When
            Mockito.`when`(service.fetchUsers()).thenReturn(Response.error(400, FakeResponseBody()))
            val repo = UserRepository(service, database)

            val result = repo.getUsersFromRemoteSource()
            assertTrue(result == null)
        }

}