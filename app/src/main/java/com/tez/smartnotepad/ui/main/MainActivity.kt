package com.tez.smartnotepad.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tez.smartnotepad.ui.note.NoteFragment
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var prefDataSource: PrefDataSource

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
        val noteFragment = NoteFragment()
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, noteFragment)
        transaction.commit()
    }
}