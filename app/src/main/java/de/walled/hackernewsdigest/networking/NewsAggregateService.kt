package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.ArticleAggregate
import de.walled.hackernewsdigest.data.NoArticles
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

class NewsAggregateService(private val aggregateApi: NewsAggregateApi) {

    fun loadTopStories(): Observable<NetworkResult> {
        return aggregateApi.loadTopStories()
                .flatMapObservable { it -> it.toAggregateResult() }
    }

    fun loadNewStories(): Observable<NetworkResult> {
        return aggregateApi.loadNewStories()
                .flatMapObservable { it -> it.toAggregateResult() }
    }

    fun loadBestStories(): Observable<NetworkResult> {
        return aggregateApi.loadBestStories()
                .flatMapObservable { it -> it.toAggregateResult() }
    }

    private fun Response<List<Long>>.toAggregateResult(): Observable<NetworkResult> {
        val networkResult = if (this.isSuccessful) {
            succesfulResponseToPayload(this)
        } else {
            NetworkResult.fromErrorResponse(this.code(), this.message())
        }
        return Observable.just(networkResult)
    }

    private fun succesfulResponseToPayload(successfulResponse: Response<List<Long>>): NetworkResult {
        val articleIds = successfulResponse.body()
        return if (articleIds != null && articleIds.isNotEmpty()) {
            NetworkResult.fromAggregateResponse(ArticleAggregate(articleIds))
        } else {
            NetworkResult.fromAggregateResponse(NoArticles)
        }
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
