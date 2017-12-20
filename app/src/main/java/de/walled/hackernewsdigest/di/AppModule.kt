package de.walled.hackernewsdigest.di

import android.app.Application
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApp(): Application = app

    @Provides
    @Singleton
    fun provideScheduler(): Scheduler = Schedulers.io()
}

