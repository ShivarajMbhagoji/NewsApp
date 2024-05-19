package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.local.NewsTypeConvertor
import com.example.newsapp.data.remote.dto.NewsApi
import com.example.newsapp.data.repository.NewsRepoImpl
import com.example.newsapp.domain.manager.LocalUserManagerImpl
import com.example.newsapp.domain.usecases.cases.AppEntryUseCases
import com.example.newsapp.domain.usecases.cases.ReadAppEntry
import com.example.newsapp.domain.usecases.cases.SaveAppEntry
import com.example.newsapp.domain.manager.LocalUserManager
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecases.news.DeleteArticle
import com.example.newsapp.domain.usecases.news.GetArticle
import com.example.newsapp.domain.usecases.news.GetArticles
import com.example.newsapp.domain.usecases.news.GetNews
import com.example.newsapp.domain.usecases.news.NewsUseCases
import com.example.newsapp.domain.usecases.news.SearchNews
import com.example.newsapp.domain.usecases.news.SelectArticle
import com.example.newsapp.domain.usecases.news.UpsertArticle
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
        newsApi: NewsApi,
        newsDao: NewsDao
    ):NewsRepository=NewsRepoImpl(newsApi,newsDao)

    @Provides
    @Singleton
    fun providesNewsUseCases(
        newsDao: NewsDao,
        newsRepository: NewsRepository
    ):NewsUseCases{
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository),
            upsertArticle = UpsertArticle(newsRepository),
            deleteArticle = DeleteArticle(newsRepository),
            getArticle = GetArticle(newsRepository),
            getArticles = GetArticles(newsRepository),
            selectArticle = SelectArticle(newsRepository)
        )
    }


    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ): NewsDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = "news_db"
        ).addTypeConverter(NewsTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        newsDatabase: NewsDatabase
    ): NewsDao = newsDatabase.newsDao

}