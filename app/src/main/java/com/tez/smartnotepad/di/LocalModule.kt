package com.tez.smartnotepad.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val PREF_FILE_NAME = "SMART"

    @Singleton
    @Provides
    fun provideSharedPrefrences(@ApplicationContext context: Context): SharedPreferences
        = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE)


}