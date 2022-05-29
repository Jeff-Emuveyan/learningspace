package com.example.uitests_hilt.util.idlingresource

import androidx.test.espresso.idling.CountingIdlingResource

/***
 * Normally, we can simply use the CountingIdlingResource class as it is naturally in our code base
 * but it will be difficult to have a test implementation of CountingIdlingResource class and a production implementation
 * because it is in a library This is why we create this container class EspressoIdlingResource below.
 * Refer to: https://www.youtube.com/watch?v=JZQtnHSNe-M
 * Refer to: https://developer.android.com/training/testing/espresso/idling-resource#integrate-alternative-approaches
 *
 * Also we don't need to extend CountingIdlingResource because it works fine for us the way it is.
 *
 * NOTE: Since this is the release(production) variant of the app, our EspressoIdlingResource is
 * not going to have any code since it is a class meant only for testing purpose not production onlike
 * the debug variant's own EspressoIdlingResource.
 */
object EspressoIdlingResource {

    fun increment() {
    }

    fun decrement() {
    }
}