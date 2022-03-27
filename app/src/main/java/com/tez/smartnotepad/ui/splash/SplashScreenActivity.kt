package com.tez.smartnotepad.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.tez.smartnotepad.ui.main.MainActivity
import com.tez.smartnotepad.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /**
        *
        * User login check
        *
        * */

        Handler().postDelayed({
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 7000)

    }
}