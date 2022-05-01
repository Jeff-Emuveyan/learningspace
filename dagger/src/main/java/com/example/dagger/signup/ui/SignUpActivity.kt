package com.example.dagger.signup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dagger.MainApplication
import com.example.dagger.R
import com.example.dagger.di.ApplicationComponent

//https://github.com/android/architecture-samples/tree/main/app/src/main/java/com/example/android/architecture/blueprints/todoapp
class SignUpActivity : AppCompatActivity() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent = (applicationContext as MainApplication).appComponent
        setContentView(R.layout.activity_login)
    }
}