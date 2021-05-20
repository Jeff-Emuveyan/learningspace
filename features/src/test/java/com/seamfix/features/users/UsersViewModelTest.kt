package com.seamfix.features.users

import com.nhaarman.mockitokotlin2.*
import com.seamfix.common.NetworkChecker
import com.seamfix.common.util.toUserEntityList
import com.seamfix.core.model.response.UserDto
import com.seamfix.core.repositories.UserRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

const val ID = "aDummyId"

class UsersViewModelTest{

    lateinit var userRepository: UserRepository
    lateinit var networkChecker: NetworkChecker

    @Before
    fun before(){
        userRepository = mock()
        networkChecker = mock()
    }

    @Test
    fun `getUsers() should save and return users gotten remote source`() = runBlocking{

        val userA = UserDto(id = ID, firstName = "JEFF")
        val userB = UserDto(id = ID, firstName = "JAMES")
        val userC = UserDto(id = ID, firstName = "JOY")
        val response = arrayListOf(userA, userB, userC)

        Mockito.`when`(userRepository.getUsersFromRemoteSource()).thenReturn(response)

        val usersViewModel = UsersViewModel(userRepository, networkChecker)
        val spyUsersViewModel = spy(usersViewModel)

        val result = spyUsersViewModel.getUsers()
        val expectedResponse = toUserEntityList(response)

        //verify that saveUsers() was called:
        verify(spyUsersViewModel, times(1)).saveUsers(any())

        assertThat(result?.size, `is`(equalTo(expectedResponse?.size)))
        assertThat(result?.get(0)?.firstName, `is`(equalTo(expectedResponse?.get(0)?.firstName)))
    }


    @Test
    fun `getUsers() should return users gotten local source`() = runBlocking{

        val userA = UserDto(id = ID, firstName = "JEFF")
        val userB = UserDto(id = ID, firstName = "JAMES")
        val userC = UserDto(id = ID, firstName = "JOY")
        val response = arrayListOf(userA, userB, userC)

        Mockito.`when`(userRepository.getUsersFromRemoteSource()).thenReturn(null)
        Mockito.`when`(userRepository.getUsersFromLocalSource()).thenReturn(toUserEntityList(response))

        val usersViewModel = UsersViewModel(userRepository, networkChecker)
        val spyUsersViewModel = spy(usersViewModel)

        val result = spyUsersViewModel.getUsers()
        val expectedResponse = toUserEntityList(response)

        //verify that saveUsers() was NOT called:
        verify(spyUsersViewModel, times(0)).saveUsers(any())

        assertThat(result?.size, `is`(equalTo(expectedResponse?.size)))
        assertThat(result?.get(0)?.firstName, `is`(equalTo(expectedResponse?.get(0)?.firstName)))

    }
}