package de.walled.hackernewsdigest.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import de.walled.hackernewsdigest.di.GsonTypeAdapters.ZonedDateTimeAdapter
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZonedDateTime
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private val gson = GsonBuilder()
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
                .create()
        private val apiVersion = "v0"
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val rxCallAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://hacker-news.firebaseio.com/$apiVersion/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxCallAdapter)
                .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }
}