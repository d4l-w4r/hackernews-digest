package de.walled.hackernewsdigest.di

import dagger.Module
import dagger.Provides
import de.walled.hackernewsdigest.networking.HackerNewsApi
import de.walled.hackernewsdigest.networking.HackerNewsController
import io.reactivex.Scheduler
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideHackerNewsController(retrofit: Retrofit, scheduler: Scheduler): HackerNewsController {
        val hackerNewsApi = retrofit.create(HackerNewsApi::class.java)
        return HackerNewsController(hackerNewsApi, scheduler)
    }
}