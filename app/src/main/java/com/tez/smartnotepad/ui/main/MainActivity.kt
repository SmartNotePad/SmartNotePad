package com.tez.smartnotepad.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tez.smartnotepad.R
import com.tez.smartnotepad.ui.register.RegisterFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerFragment: RegisterFragment = RegisterFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, registerFragment)
        transaction.commit()
    }
}