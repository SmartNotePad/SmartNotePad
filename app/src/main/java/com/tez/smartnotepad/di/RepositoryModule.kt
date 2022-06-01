package com.tez.smartnotepad.di

import com.tez.smartnotepad.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideNoteRepostiory(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository


    @Binds
    @Singleton
    abstract fun provideContentRepository(contentRepositoryImpl: ContentRepositoryImpl): ContentRepository

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}