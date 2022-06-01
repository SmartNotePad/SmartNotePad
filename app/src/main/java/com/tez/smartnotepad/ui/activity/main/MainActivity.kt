package com.tez.smartnotepad.ui.activity.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.ui.fragment.home.HomeFragment
import com.tez.smartnotepad.ui.fragment.login.LoginFragment
import com.tez.smartnotepad.util.ext.name
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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