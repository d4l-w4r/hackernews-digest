package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsItem
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.http.GET

class HackerNewsController(private val api: HackerNewsApi, private val scheduler: Scheduler) {

    fun loadTopStories(): Single<List<HackerNewsItem>> {
        return api.loadTopStories()
                .map { articleIdList -> articleIdList.mapToNewsItems() }
                .subscribeOn(scheduler)
    }

    fun loadNewStories(): Single<List<HackerNewsItem>> {
        return api.loadNewStories()
                .map { articleIdList -> articleIdList.mapToNewsItems() }
                .subscribeOn(scheduler)
    }

    fun loadBestStories(): Single<List<HackerNewsItem>> {
        return api.loadBestStories()
                .map { articleIdList -> articleIdList.mapToNewsItems() }
                .subscribeOn(scheduler)
    }

    private fun List<Long>.mapToNewsItems(): List<HackerNewsItem> = this.map { HackerNewsItem(it) }
}

interface HackerNewsApi {
    @GET("topstories.json")
    fun loadTopStories(): Single<List<Long>>

    @GET("newstories.json")
    fun loadNewStories(): Single<List<Long>>

    @GET("beststories.json")
    fun loadBestStories(): Single<List<Long>>
}
