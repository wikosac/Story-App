package com.dicoding.myunlimitedquotes.di

import android.content.Context
import com.dicoding.myunlimitedquotes.database.StoryDatabase
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}