package de.walled.hackernewsdigest.di

import dagger.Component
import de.walled.hackernewsdigest.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        ApiModule::class,
        NetworkModule::class))

interface HackernewsComponent {

    fun inject(mainActivity: MainActivity)
}