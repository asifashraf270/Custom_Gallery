package com.customgallery.di

import android.content.ContentResolver
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
object MediaModule {


    @Provides
    fun providerContentResolver(@ApplicationContext context: Context):ContentResolver{
        return context.contentResolver;
    }
}