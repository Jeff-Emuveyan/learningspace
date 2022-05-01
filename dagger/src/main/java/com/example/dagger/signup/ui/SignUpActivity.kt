package com.example.dagger.signup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dagger.MainApplication
import com.example.dagger.R
import com.example.dagger.di.ApplicationComponent

class SignUpActivity : AppCompatActivity() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent = (applicationContext as MainApplication).appComponent
        setContentView(R.layout.activity_login)
    }
}