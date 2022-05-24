package com.tez.smartnotepad.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var prefDataSource: PrefDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        prefDataSource = PrefDataSource(this.getSharedPreferences("SMART", Context.MODE_PRIVATE))

        Handler().postDelayed({
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("Logged",prefDataSource.isUserLogged())
            startActivity(intent)
            onStop()
        }, 4000)
    }
}