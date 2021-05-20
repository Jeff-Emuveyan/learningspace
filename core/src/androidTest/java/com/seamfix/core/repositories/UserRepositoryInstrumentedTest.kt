package com.seamfix.core.repositories
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.seamfix.core.model.table.User
import com.seamfix.core.source.local.AppDatabase
import com.seamfix.core.util.FakeService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

const val USER_ID = "USER_ID"

/**
 * This class tests the Room database of the app through the UserDAO.
 * NOTE: An ANDROID DEVICE must be connected to the IDE in other to run this instrumented tests.
 * **/
@RunWith(AndroidJUnit4::class)
class UserRepositoryInstrumentedTest{

    private lateinit var repository: UserRepository
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val service = FakeService()
        repository = UserRepository(service, db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun can_save_a_user_to_database() = runBlocking{

        //create a user
        val user = User(id = USER_ID, firstName = "JEFF")

        //save the user
        repository.save(user)

        //check that the user was saved
        val savedUser = repository.getUserFromLocalSource(USER_ID)
        assertThat(savedUser?.id, `is`(equalTo(user.id)))
    }


    @Test
    @Throws(Exception::class)
    fun can_update_a_user() = runBlocking {

        //create a user
        val user = User(id = USER_ID, firstName = "JEFF")

        //save the user
        repository.save(user)

        //update the user:
        val userUpdate = User(id = USER_ID, firstName = "JEFF", lastName = "Emuveyan")

        //save the user update
        repository.save(userUpdate)

        //confirm that the update was done:
        val savedUser = repository.getUserFromLocalSource(USER_ID)
        assertThat(savedUser?.lastName, `is`(equalTo("Emuveyan")))
    }


    @Test
    @Throws(Exception::class)
    fun can_get_all_users() = runBlocking{

        //create a users
        val userA = User(id = "1", firstName = "JEFF A")
        val userB = User(id = "2", firstName = "JEFF B")
        val userC = User(id = "3", firstName = "JEFF C")

        //save the users:
        repository.save(userA)
        repository.save(userB)
        repository.save(userC)

        //confirm that the users were saved:
        val savedUsers = repository.getUsersFromLocalSource()
        assertThat(savedUsers?.size, `is`(equalTo(3)))
    }
}