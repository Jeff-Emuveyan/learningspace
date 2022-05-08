package com.example.uitests_hilt

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uitests_hilt.ui.list.ListOfCitiesFragment
import com.example.uitests_hilt.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListOfCitiesFragmentInstrumentedTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun test_that_info_text_is_shown() {
        val scenario = launchFragmentInHiltContainer<ListOfCitiesFragment>()

        assertEquals("com.example.uitests_hilt", appContext.packageName)
    }
}