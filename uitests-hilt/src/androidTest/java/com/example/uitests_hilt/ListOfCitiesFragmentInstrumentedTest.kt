package com.example.uitests_hilt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uitests_hilt.hilt.AppDatabaseModule
import com.example.uitests_hilt.ui.list.ListOfCitiesFragment
import com.example.uitests_hilt.ui.list.ListOfCitiesViewModel
import com.example.uitests_hilt.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

/***
 * I used mockk instead of mockito-android because mockito-android threw errors when I tried to mock
 * methods and it outright fell apart when I tried to dependency inject a mock by attaching @BindValue @JvmField
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListOfCitiesFragmentInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue @JvmField
    val viewModel:ListOfCitiesViewModel = mockk<ListOfCitiesViewModel>(relaxed = true)

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
        launchFragmentInHiltContainer<ListOfCitiesFragment>()
    }

    @Test
    fun test_that_proper_network_error_message_is_show_when_there_is_network_error() {

        onView(withId(R.id.tvSearch)).perform(typeText("Hello Jeff"))
        onView(withText("paul is bad")).check(matches(isDisplayed()))
    }
}