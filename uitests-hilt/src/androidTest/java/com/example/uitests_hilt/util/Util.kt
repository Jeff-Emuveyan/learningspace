package com.example.uitests_hilt.util

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.uitests_hilt.HiltTestActivity
import com.example.uitests_hilt.R
import com.example.uitests_hilt.model.entity.CityEntity

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {},
    @NavigationRes navigationGraphId: Int? = null

): TestNavHostController? {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(ApplicationProvider.getApplicationContext(), HiltTestActivity::class.java)
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    var navController: TestNavHostController? = null
    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader), T::class.java.name)

        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
        fragment.action()

        if (navigationGraphId != null) {
            navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            // Set the graph on the TestNavHostController
            navController?.setGraph(R.navigation.cities_nav_graph)
            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }
    return navController
}

val listOfCities = mutableListOf<CityEntity>().apply {
    add(CityEntity(id = 0, name = "0-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "1-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "2-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "3-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "4-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "5-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "6-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "7-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "8-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "9-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "10-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "11-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Lagos", localName = "Lekki", countryName = "Nigeria", lat = 33.333, lng = 12.432))
    add(CityEntity(id = 3, name = "13-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "14-Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 0, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 1, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 2, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 3, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
    add(CityEntity(id = 4, name = "Rome", localName = "Lil Italy", countryName = "Italy"))
}