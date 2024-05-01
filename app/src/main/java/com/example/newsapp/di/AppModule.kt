package com.example.newsapp.di

import android.app.Application
import com.example.newsapp.data.remote.dto.NewsApi
import com.example.newsapp.data.repository.NewsRepoImpl
import com.example.newsapp.domain.manager.LocalUserManagerImpl
import com.example.newsapp.domain.usecases.cases.AppEntryUseCases
import com.example.newsapp.domain.usecases.cases.ReadAppEntry
import com.example.newsapp.domain.usecases.cases.SaveAppEntry
import com.example.newsapp.domain.manager.LocalUserManager
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecases.news.GetNews
import com.example.newsapp.domain.usecases.news.NewsUseCases
import com.example.newsapp.utils.constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun providesLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun providesNewsApi():NewsApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRepository(
        newsApi: NewsApi
    ):NewsRepository=NewsRepoImpl(newsApi)

    @Provides
    @Singleton
    fun providesNewsUseCases(
        newsRepository: NewsRepository
    ):NewsUseCases{
        return NewsUseCases(
            getNews = GetNews(newsRepository)
        )
    }
}