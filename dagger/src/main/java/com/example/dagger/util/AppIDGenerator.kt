package com.example.dagger.util

import java.security.SecureRandom
import javax.inject.Inject

class AppIDGenerator @Inject constructor() {

    var getId: Int? = null

    init {
        getId = SecureRandom().nextInt()
    }
}