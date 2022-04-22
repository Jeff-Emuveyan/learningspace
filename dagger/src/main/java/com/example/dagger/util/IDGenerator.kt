package com.example.dagger.util

import java.security.SecureRandom

class IDGenerator() {

    var getId: Int? = null

    init {
        getId = SecureRandom().nextInt()
    }
}