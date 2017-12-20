package de.walled.hackernewsdigest

import android.app.Application
import de.walled.hackernewsdigest.di.AppModule
import de.walled.hackernewsdigest.di.DaggerHackernewsComponent
import de.walled.hackernewsdigest.di.HackernewsComponent


class HackerNewsApp : Application() {

    companion object {
        lateinit var hackerNewsComponent: HackernewsComponent
    }

    override fun onCreate() {
        super.onCreate()
        hackerNewsComponent = DaggerHackernewsComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}