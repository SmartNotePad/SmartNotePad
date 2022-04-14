package com.tez.smartnotepad.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.ui.home.HomeFragment
import com.tez.smartnotepad.ui.login.LoginFragment
import com.tez.smartnotepad.util.ext.name

class MainActivity : AppCompatActivity() {

    private lateinit var prefDataSource: PrefDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefDataSource = PrefDataSource(this.getSharedPreferences("SMART", Context.MODE_PRIVATE))

        val isLogged = intent.getBooleanExtra("Logged",false)
        if (isLogged) goNotesFragment() else goLoginFragment()
    }

    private fun goLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, loginFragment)
        transaction.commit()
    }

    private fun goNotesFragment() {
        val noteFragment = HomeFragment()
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, noteFragment)
        transaction.addToBackStack(noteFragment.name())
        transaction.commit()
    }

    override fun onBackPressed() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is HomeFragment) {
                return
            }
            supportFragmentManager.popBackStackImmediate()
        }
    }
}