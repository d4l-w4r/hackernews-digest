package de.walled.hackernewsdigest.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        ApiModule::class,
        NetworkModule::class))

interface HackernewsComponent {

}