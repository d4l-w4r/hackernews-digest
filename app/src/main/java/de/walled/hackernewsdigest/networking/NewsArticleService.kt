package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsItem
import de.walled.hackernewsdigest.data.NoArticle
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class NewsArticleService(val articleApi: NewsArticleApi) {

    fun loadArticle(articleId: Long): Single<NetworkResult> {
        return articleApi.loadArticle(articleId)
                .map { it.toArticleResult() }
    }

    fun Response<HackerNewsItem>.toArticleResult() =
            if (this.isSuccessful) {
                NetworkResult.fromArticleResponse(this.body() ?: NoArticle)
            } else {
                NetworkResult.fromErrorResponse(this.code(), this.message())
            }
}


interface NewsArticleApi {

    @GET("item/{id}.json")
    fun loadArticle(@Path("id") id: Long): Single<Response<HackerNewsItem>>
}
