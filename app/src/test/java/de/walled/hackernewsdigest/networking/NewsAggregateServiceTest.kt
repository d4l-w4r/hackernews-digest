package de.walled.hackernewsdigest.networking

import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response


class NewsAggregateServiceTest {

    @Test
    fun `Given a list of ids when loading best stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(1, 2, 3, 4, 5, 6, 7, 8)
        val testController = NewsAggregateService(createTestValueAPI(expectedIds))
        testController.loadBestStories()
                .map { assertTrue(it is Payload.AggregateArticles && it == expectedIds) }
                .subscribe()
    }

    @Test
    fun `Given a list of ids when loading top stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(9, 10, 11, 12, 13, 14, 15, 16, 17)
        val testController = NewsAggregateService(createTestValueAPI(expectedIds))
        testController.loadTopStories()
                .map { assertTrue(it is Payload.AggregateArticles && it == expectedIds) }
                .subscribe()
    }

    @Test
    fun `Given a list of ids when loading new stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(18, 19, 20, 21, 22, 23, 24, 25, 26)
        val testController = NewsAggregateService(createTestValueAPI(expectedIds))
        testController.loadNewStories()
                .map { assertTrue(it is Payload.AggregateArticles && it == expectedIds) }
                .subscribe()
    }

    @Test
    fun `Given a call returns a 404 error then the NetworkResult will have the type ResourceNotFound`() {
        val testController = NewsAggregateService(createErrorAPI { createResourceNotFoundResponse() })
        testController.loadNewStories()
                .map { assertTrue(it is HttpError.ResourceNotFound) }
                .subscribe()
        testController.loadBestStories()
                .map { assertTrue(it is HttpError.ResourceNotFound) }
                .subscribe()
        testController.loadTopStories()
                .map { assertTrue(it is HttpError.ResourceNotFound) }
                .subscribe()
    }

    @Test
    fun `Given a call returns a 503 error then the NetworkResult will have the type ServiceUnavailable`() {
        val testController = NewsAggregateService(createErrorAPI { createServiceUnavailableResponse() })
        testController.loadNewStories()
                .map { assertTrue(it is HttpError.ServiceUnavailable) }
                .subscribe()
        testController.loadBestStories()
                .map { assertTrue(it is HttpError.ServiceUnavailable) }
                .subscribe()
        testController.loadTopStories()
                .map { assertTrue(it is HttpError.ServiceUnavailable) }
                .subscribe()
    }

    @Test
    fun `Given a call returns an unknown error then the NetworkResult will have the type UnknownError`() {
        val testController = NewsAggregateService(createErrorAPI { createUnknownErrorResponse() })
        testController.loadNewStories()
                .map { assertTrue(it is HttpError.UnknownError && it.message == expectedUnknownErrorText) }
                .subscribe()
        testController.loadBestStories()
                .map { assertTrue(it is HttpError.UnknownError && it.message == expectedUnknownErrorText) }
                .subscribe()
        testController.loadTopStories()
                .map { assertTrue(it is HttpError.UnknownError && it.message == expectedUnknownErrorText) }
                .subscribe()
    }


    companion object {
        fun createSuccesfulResponseWithData(data: List<Long>): Single<Response<List<Long>>> {
            return Observable.create<Response<List<Long>>> { }.single(Response.success(data))
        }

        fun createResourceNotFoundResponse(): Single<Response<List<Long>>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), "")
            val error = Response.error<List<Long>>(404, errorBody)
            return Observable.create<Response<List<Long>>> { }.single(error)
        }

        fun createServiceUnavailableResponse(): Single<Response<List<Long>>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), "")
            val error = Response.error<List<Long>>(503, errorBody)
            return Observable.create<Response<List<Long>>> { }.single(error)
        }

        fun createUnknownErrorResponse(): Single<Response<List<Long>>> {
            val errorBody = ResponseBody.create(MediaType.parse("text/plaintext"), expectedUnknownErrorText)
            val error = Response.error<List<Long>>(999, errorBody)
            return Observable.create<Response<List<Long>>> { }.single(error)
        }

        val expectedUnknownErrorText = "I am an unknown error."
    }

    private fun createTestValueAPI(expectedStoryIds: List<Long>): NewsAggregateApi {

        return object : NewsAggregateApi {
            override fun loadTopStories(): Single<Response<List<Long>>> {
                return createSuccesfulResponseWithData(expectedStoryIds)
            }

            override fun loadNewStories(): Single<Response<List<Long>>> {
                return createSuccesfulResponseWithData(expectedStoryIds)
            }

            override fun loadBestStories(): Single<Response<List<Long>>> {
                return createSuccesfulResponseWithData(expectedStoryIds)
            }

        }
    }

    private fun createErrorAPI(errorFunction: () -> Single<Response<List<Long>>>): NewsAggregateApi {
        return object : NewsAggregateApi {
            override fun loadTopStories(): Single<Response<List<Long>>> {
                return errorFunction()
            }

            override fun loadNewStories(): Single<Response<List<Long>>> {
                return errorFunction()
            }

            override fun loadBestStories(): Single<Response<List<Long>>> {
                return errorFunction()
            }

        }
    }
}