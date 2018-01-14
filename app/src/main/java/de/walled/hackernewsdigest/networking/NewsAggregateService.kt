package de.walled.hackernewsdigest.networking

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

class NewsAggregateService(private val aggregateApi: NewsAggregateApi) {

    fun loadTopStories(): Single<NetworkResult> {
        return aggregateApi.loadTopStories()
                .map { it.toAggregateResult() }
    }

    fun loadNewStories(): Single<NetworkResult> {
        return aggregateApi.loadNewStories()
                .map { it.toAggregateResult() }
    }

    fun loadBestStories(): Single<NetworkResult> {
        return aggregateApi.loadBestStories()
                .map { it.toAggregateResult() }
    }

    private fun Response<List<Long>>.toAggregateResult() =
            if (this.isSuccessful) {
                NetworkResult.fromAggregateResponse(this.body() ?: emptyList())
            } else {
                NetworkResult.fromErrorResponse(this.code(), this.message())
            }
}

interface NewsAggregateApi {
    @GET("topstories.json")
    fun loadTopStories(): Single<Response<List<Long>>>

    @GET("newstories.json")
    fun loadNewStories(): Single<Response<List<Long>>>

    @GET("beststories.json")
    fun loadBestStories(): Single<Response<List<Long>>>
}
