package com.tez.smartnotepad.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var prefDataSource: PrefDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Logged", prefDataSource.isUserLogged())
            startActivity(intent)
            onStop()
        }, 4000)
    }
}