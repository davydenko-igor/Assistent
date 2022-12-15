package com.example.assistent.di

import android.app.Application
import androidx.room.Room
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.db.AssistentDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): AssistentDatabase {
        return Room.databaseBuilder(application, AssistentDatabase::class.java, "assistdb")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideAssistentDAO(database: AssistentDatabase): AssistentDAO {
        return database.getAssistentDAO
    }

    single { provideDatabase(androidApplication()) }
    single { provideAssistentDAO(get()) }
}