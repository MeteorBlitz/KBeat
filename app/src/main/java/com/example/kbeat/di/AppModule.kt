package com.example.kbeat.di

import android.content.Context
import androidx.room.Room
import com.example.kbeat.data.local.FavoritesDao
import com.example.kbeat.data.local.KBeatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KBeatDatabase {
        return Room.databaseBuilder(
            context,
            KBeatDatabase::class.java,
            "kbeat_db"
        ).build()
    }

    @Provides
    fun provideFavoritesDao(db: KBeatDatabase): FavoritesDao {
        return db.favoritesDao()
    }
}