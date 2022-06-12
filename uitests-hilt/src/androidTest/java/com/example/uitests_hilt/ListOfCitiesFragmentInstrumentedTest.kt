package com.example.uitests_hilt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.uitests_hilt.data.repository.CityRepository
import com.example.uitests_hilt.model.dto.Result
import com.example.uitests_hilt.model.dto.ui.UIStateType
import com.example.uitests_hilt.ui.list.CityItem
import com.example.uitests_hilt.ui.list.ListOfCitiesFragment
import com.example.uitests_hilt.ui.list.ListOfCitiesViewModel
import com.example.uitests_hilt.util.idlingresource.EspressoIdlingResource
import com.example.uitests_hilt.util.launchFragmentInHiltContainer
import com.example.uitests_hilt.util.listOfCities
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.flow.flow
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

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

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject // This tells hilt to give us a real production instance of a repository
    lateinit var injectedRepository: CityRepository

    private lateinit var repository: CityRepository // We created this field because we want to spy
    // the injectedRepository.

    @Inject
    lateinit var espressoIdlingResource: EspressoIdlingResource

    /**
     * Most times, when launching a fragment for test, it is good to mock the viewModel.
     * Below are several styles of mocking the viewModel.
     *
     * Refer to this link: https://developer.android.com/training/dependency-injection/hilt-testing#inject-in-tests
     */

    /*
        If you just want to completely mock the viewModel itself do:
        @BindValue @JvmField
        val viewModel:ListOfCitiesViewModel = mockk<ListOfCitiesViewModel>(relaxed = true)

        Where the @BindValue tells hilt to replace the production instance with the one we
        provided here.
    */

    /*
        If you just want to mock the dependencies of the viewModel (eg repository) and you
        also want to mock the viewModel too, do:
        @BindValue @JvmField
        val viewModel:ListOfCitiesViewModel = spyk(ListOfCitiesViewModel(repository))
        (Where the 'repository' is a mocked object ie repository: CityRepository = mockk())
    */

    /*
        If you want the viewModel to NOT be mocked but want it's dependencies to be mocked (eg repository)
        (Where the 'repository' is a mocked object ie repository: CityRepository = mockk()) or
        (Where repository = spyk(injectedRepository)) if you want to spy instead.
     */
    @BindValue
    lateinit var viewModel:ListOfCitiesViewModel
    // Note: The @BindValue we used above is just to tell hilt to replace the viewModel it would
    // have injected for us with the one we created ourselves ie we are doing an overwrite.

    @Before
    fun setUp() {
        // Populate @Inject fields in test class.
        // This line is very important because every injected field will be null until it is called.
        // For example, 'injectedRepository' variable will be null until hiltRule.inject() is called.
        // This is why we put repository = spyk(injectedRepository) after hiltRule.inject().
        hiltRule.inject()

        repository = spyk(injectedRepository)
        viewModel = ListOfCitiesViewModel(espressoIdlingResource, repository)

        IdlingRegistry.getInstance().register(espressoIdlingResource.idlingResource);
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

    /**
     * This test is to ensure that we can use idling resource to wait for a long running task to
     * complete and verify the result.
     */
    @Test
    fun test_that_a_long_running_task_can_be_done_and_completed_successfully() {
        every { repository.fetchAndCacheCities(any())} returns flow { emit(Result(UIStateType.SUCCESS, listOfCities)) }

        launchFragmentInHiltContainer<ListOfCitiesFragment>()

        // Verify the the progress bar for long running tasks is hidden
        onView(withId(R.id.progressBarTwo)).check(matches(not(isDisplayed())))

        // Click on the button
        onView(withId(R.id.button)).perform(click())

        // Check that the text on the button has changed to "Completed"
        onView(withId(R.id.button)).check(matches(withText("Completed")))
    }
}