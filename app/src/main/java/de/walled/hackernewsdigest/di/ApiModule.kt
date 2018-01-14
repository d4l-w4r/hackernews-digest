package de.walled.hackernewsdigest.di

import dagger.Module
import dagger.Provides
import de.walled.hackernewsdigest.networking.NewsAggregateApi
import de.walled.hackernewsdigest.networking.NewsAggregateService
import de.walled.hackernewsdigest.networking.NewsArticleApi
import de.walled.hackernewsdigest.networking.NewsArticleService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideNewsAggregateService(retrofit: Retrofit): NewsAggregateService {
        val aggregateApi = retrofit.create(NewsAggregateApi::class.java)
        return NewsAggregateService(aggregateApi)
    }

    @Provides
    @Singleton
    fun provideNewsArticleService(retrofit: Retrofit): NewsArticleService {
        val articleApi = retrofit.create(NewsArticleApi::class.java)
        return NewsArticleService(articleApi)
    }
}