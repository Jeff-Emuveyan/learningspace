package com.seamfix.features.users.userdetail
import com.nhaarman.mockitokotlin2.*
import com.seamfix.common.NetworkChecker
import com.seamfix.common.util.toUserEntityList
import com.seamfix.core.model.response.UserDto
import com.seamfix.core.repositories.UserRepository
import com.seamfix.features.users.ID
import com.seamfix.features.users.UsersViewModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import junit.framework.TestCase
const val ID = "aDummyId"

class UserDetailViewModelTest {

    lateinit var userRepository: UserRepository
    lateinit var networkChecker: NetworkChecker

    @Before
    fun before(){
        userRepository = mock()
        networkChecker = mock()
    }

    @Test
    fun `getUser() should save and return a user gotten remote source`() = runBlocking{

        val userA = UserDto(id = ID, firstName = "JEFF")

        Mockito.`when`(userRepository.getUserFromRemoteSource(ID)).thenReturn(userA)

        val userDetailViewModel = UserDetailViewModel(userRepository, networkChecker)
        val spyUserDetailViewModel = spy(userDetailViewModel)

        val result = spyUserDetailViewModel.getUser(ID)
        val expectedResponse = userA.toEntity()

        //verify that saveUsers() was called:
        verify(userRepository, times(1)).save(any())
        assertThat(result?.firstName, `is`(equalTo(expectedResponse.firstName)))
    }


    @Test
    fun `getUser() should return a user gotten local source`() = runBlocking{

        val userA = UserDto(id = ID, firstName = "JEFF")

        Mockito.`when`(userRepository.getUserFromRemoteSource(ID)).thenReturn(null)
        Mockito.`when`(userRepository.getUserFromLocalSource(ID)).thenReturn(userA.toEntity())

        val userDetailViewModel = UserDetailViewModel(userRepository, networkChecker)
        val spyUserDetailViewModel = spy(userDetailViewModel)

        val result = spyUserDetailViewModel.getUser(ID)
        val expectedResponse = userA.toEntity()

        //verify that saveUsers() was NOT called:
        verify(userRepository, times(0)).save(any())
        assertThat(result?.firstName, `is`(equalTo(expectedResponse.firstName)))
    }

}