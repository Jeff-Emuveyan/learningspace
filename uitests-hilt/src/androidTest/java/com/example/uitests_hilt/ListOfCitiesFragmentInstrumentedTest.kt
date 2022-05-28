package com.example.uitests_hilt

import android.util.Log
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uitests_hilt.data.repository.CityRepository
import com.example.uitests_hilt.hilt.AppDatabaseModule
import com.example.uitests_hilt.model.dto.Query
import com.example.uitests_hilt.model.dto.QueryType
import com.example.uitests_hilt.model.dto.Result
import com.example.uitests_hilt.model.dto.ui.UIStateType
import com.example.uitests_hilt.ui.list.CityItem
import com.example.uitests_hilt.ui.list.ListOfCitiesFragment
import com.example.uitests_hilt.ui.list.ListOfCitiesViewModel
import com.example.uitests_hilt.util.launchFragmentInHiltContainer
import com.example.uitests_hilt.util.listOfCities
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flow
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

/***
 * I used mockk instead of mockito-android because mockito-android threw errors when I tried to mock
 * methods and it outright fell apart when I tried to dependency inject a mock by attaching @BindValue @JvmField
 */


/***
 * Espresso test for RecyclerView can be found here:
 * https://github.com/android/testing-samples/blob/main/ui/espresso/RecyclerViewSample/app/src/androidTest/java/com/example/android/testing/espresso/RecyclerViewSample/RecyclerViewSampleTest.java
 * See also: https://developer.android.com/guide/navigation/navigation-testing#groovy
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListOfCitiesFragmentInstrumentedTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val repository: CityRepository = mockk()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    /*
        If you just want to completely mock the viewModel itself:
        @BindValue @JvmField
        val viewModel:ListOfCitiesViewModel = mockk<ListOfCitiesViewModel>(relaxed = true)
    */

    /*
        If you just want to mock the dependencies of the viewModel (eg repository) and you
        also want to mock the viewModel too:
        @BindValue @JvmField
        val viewModel:ListOfCitiesViewModel = spyk(ListOfCitiesViewModel(repository))
        (Where the 'repository' is a mocked object)
    */

    /*
        If you want the viewModel to NOT be mocked by want it's dependencies to be mocked (eg repository)
        (Where the 'repository' is a mocked object):
     */
    @BindValue @JvmField
    val viewModel:ListOfCitiesViewModel = ListOfCitiesViewModel(repository)

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun test_that_proper_network_error_message_is_show_when_there_is_network_error() {
        every { repository.fetchAndCacheCities(any())} returns flow { emit(Result(UIStateType.NETWORK_ERROR)) }

        launchFragmentInHiltContainer<ListOfCitiesFragment>()

        // check for visibility:
        onView(withId(R.id.tvInfo)).check(matches(isDisplayed()))
        // check that the text is correct:
        onView(withId(R.id.tvInfo)).check(matches(withText(appContext.getString(R.string.msg_network_error))))
    }

    /**
     * See the link above for resource material on this recyclerview test
     */
    @Test
    fun test_that_data_can_be_displayed_on_the_recyclerView() {
        every { repository.fetchAndCacheCities(any())} returns flow { emit(Result(UIStateType.SUCCESS, listOfCities)) }
        // Our test will require us to handle navigation because that is what happens when the user
        // clicks on an item on the list.
        // Because of this, we need to set a NavController to our fragment else there will be crashes
        // when the click occurs:
        val navHostController =
            launchFragmentInHiltContainer<ListOfCitiesFragment>(navigationGraphId = R.navigation.cities_nav_graph)

        // the info textView should be hidden:
        onView(withId(R.id.tvInfo)).check(matches(not(isDisplayed())))

        // First scroll to the position that needs to be matched and click on it.
        // For this test, we want to scroll to and click on the 13th element on the list.
        // (We have already set the city on the list at the 13th position to be Lagos).
        // Note: 13th position will item number 12 on a list.
        // *actionOnItemAtPosition* scrolls the recycler view to the specified position:
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CityItem>(12, click()))

        // Verify that we have scrolled to Lagos state
        onView(withText("Lagos")).check(matches(isDisplayed()))

        // Verify that we are navigated to the right fragment (MapOfCitiesFragment) after clicking:
        assertEquals(navHostController?.currentDestination?.id, R.id.mapOfCitiesFragment)

        // Verify that the correct data bundle is sent to MapOfCitiesFragment:
       // val arguments = navHostController?.currentDestination?.arguments
        val arguments = navHostController?.backStack?.last()?.arguments
        val latitude = arguments?.get("latitude")
        val longitude = arguments?.get("longitude")
        val cityName = arguments?.get("cityName")
        val countryName = arguments?.get("countryName")

        assertEquals(latitude, 33.333f)
        assertEquals(longitude, 12.432f)
        assertEquals(cityName, "Lagos")
        assertEquals(countryName, "Nigeria")
    }
}