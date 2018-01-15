package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsArticle
import de.walled.hackernewsdigest.data.NoArticle
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response


class NewsArticleServiceTest {

    @Test
    fun `Given a call returns successfully then the result will be a SingleArticle with data`() {
        val expectedItem = HackerNewsArticle(42)
        val articleService = NewsArticleService(createTestAPI { createSuccesfulResponseWithData(expectedItem) })
        articleService.loadArticle(42)
                .map { assertTrue(it is Payload.SingleArticle && it.article == expectedItem) }
                .subscribe()
    }

    @Test
    fun `Given a call returns successfully but without data then the result will be NoArticle`() {
        val articleService = NewsArticleService(createTestAPI { createSuccesfulResponseWithoutData() })
        articleService.loadArticle(3)
                .map { assertTrue(it is Payload.SingleArticle && it.article is NoArticle) }
                .subscribe()
    }

    @Test
    fun `Given a call returns a 404 error then the NetworkResult will have the type ResourceNotFound`() {
        val articleService = NewsArticleService(createTestAPI { createResourceNotFoundResponse() })
        articleService.loadArticle(1)
                .map { assertTrue(it is HttpError.ResourceNotFound) }
                .subscribe()
    }

    @Test
    fun `Given a call returns a 503 error then the NetworkResult will have the type ServiceUnavailable`() {
        val articleService = NewsArticleService(createTestAPI { createServiceUnavailableResponse() })
        articleService.loadArticle(4)
                .map { assertTrue(it is HttpError.ServiceUnavailable) }
                .subscribe()
    }

    @Test
    fun `Given a call returns an unknown error then the NetworkResult will have the type UnknownError`() {
        val articleService = NewsArticleService(createTestAPI { createResourceNotFoundResponse() })
        articleService.loadArticle(942)
                .map { assertTrue(it is HttpError.UnknownError && it.message == expectedUnknownErrorText) }
                .subscribe()
    }

    private fun createTestAPI(resultFunction: () -> Single<Response<HackerNewsArticle>>): NewsArticleApi {
        return object : NewsArticleApi {
            override fun loadArticle(id: Long): Single<Response<HackerNewsArticle>> {
                return resultFunction()
            }
        }
    }

    companion object {
        fun createSuccesfulResponseWithData(data: HackerNewsArticle): Single<Response<HackerNewsArticle>> {
            return Observable.create<Response<HackerNewsArticle>> { }.single(Response.success(data))
        }

        fun createSuccesfulResponseWithoutData(): Single<Response<HackerNewsArticle>> {
            return Observable.create<Response<HackerNewsArticle>> { }.single(Response.success(null))
        }

        fun createResourceNotFoundResponse(): Single<Response<HackerNewsArticle>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), "")
            val error = Response.error<HackerNewsArticle>(404, errorBody)
            return Observable.create<Response<HackerNewsArticle>> { }.single(error)
        }

        fun createServiceUnavailableResponse(): Single<Response<HackerNewsArticle>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), "")
            val error = Response.error<HackerNewsArticle>(503, errorBody)
            return Observable.create<Response<HackerNewsArticle>> { }.single(error)
        }

        fun createUnknownErrorResponse(): Single<Response<HackerNewsArticle>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), expectedUnknownErrorText)
            val error = Response.error<HackerNewsArticle>(999, errorBody)
            return Observable.create<Response<HackerNewsArticle>> { }.single(error)
        }

        val expectedUnknownErrorText = "I am an unknown error."
    }
}