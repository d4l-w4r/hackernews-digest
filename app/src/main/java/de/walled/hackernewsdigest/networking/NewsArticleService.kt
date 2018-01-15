package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsArticle
import de.walled.hackernewsdigest.data.NoArticle
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class NewsArticleService(val articleApi: NewsArticleApi) {

    fun loadArticle(articleId: Long): Observable<NetworkResult> {
        return articleApi.loadArticle(articleId)
                .flatMapObservable { it.toArticleResult() }
    }

    fun Response<HackerNewsArticle>.toArticleResult(): Observable<NetworkResult> {
        val networkResult = if (this.isSuccessful) {
            NetworkResult.fromArticleResponse(this.body() ?: NoArticle)
        } else {
            NetworkResult.fromErrorResponse(this.code(), this.message())
        }
        return Observable.just(networkResult)
    }
}


interface NewsArticleApi {

    @GET("item/{id}.json")
    fun loadArticle(@Path("id") id: Long): Single<Response<HackerNewsArticle>>
}
