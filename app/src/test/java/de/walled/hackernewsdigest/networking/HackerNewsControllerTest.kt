package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.HackerNewsItem
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertTrue
import org.junit.Test


class HackerNewsControllerTest {

    @Test
    fun `Given a list of ids when loading best stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(1, 2, 3, 4, 5, 6, 7, 8)
        val testController = HackerNewsController(createDummyApi(expectedIds), testScheduler)
        testController.loadBestStories()
                .map { assertTrue(it.hasAllStoryIds(expectedIds)) }
                .subscribe()
    }

    @Test
    fun `Given a list of ids when loading top stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(9, 10, 11, 12, 13, 14, 15, 16, 17)
        val testController = HackerNewsController(createDummyApi(expectedIds), testScheduler)
        testController.loadTopStories()
                .map { assertTrue(it.hasAllStoryIds(expectedIds)) }
                .subscribe()
    }

    @Test
    fun `Given a list of ids when loading new stories then the result will be a list of items with these ids`() {
        val expectedIds = listOf<Long>(18, 19, 20, 21, 22, 23, 24, 25, 26)
        val testController = HackerNewsController(createDummyApi(expectedIds), testScheduler)
        testController.loadNewStories()
                .map { assertTrue(it.hasAllStoryIds(expectedIds)) }
                .subscribe()
    }

    private fun List<HackerNewsItem>.hasAllStoryIds(expectedStoryIds: List<Long>): Boolean {
        return mapIndexed { index, newsItem ->
            newsItem.id == expectedStoryIds[index]
        }.all { it }
    }

    companion object {
        val testScheduler = Schedulers.single()
    }

    private fun createDummyApi(expectedStoryIds: List<Long>): HackerNewsApi {
        return object : HackerNewsApi {
            override fun loadTopStories(): Single<List<Long>> {
               return Observable.fromIterable(expectedStoryIds).toList()
            }

            override fun loadNewStories(): Single<List<Long>> {
                return Observable.fromIterable(expectedStoryIds).toList()
            }

            override fun loadBestStories(): Single<List<Long>> {
                return Observable.fromIterable(expectedStoryIds).toList()
            }

        }
    }
}