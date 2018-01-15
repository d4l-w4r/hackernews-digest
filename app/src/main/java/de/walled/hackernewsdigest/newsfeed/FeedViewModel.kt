package de.walled.hackernewsdigest.newsfeed

import android.arch.lifecycle.ViewModel
import android.util.Log
import de.walled.hackernewsdigest.data.*
import de.walled.hackernewsdigest.networking.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class FeedViewModel(
        private val aggregateService: NewsAggregateService,
        private val articleService: NewsArticleService) : ViewModel() {

    private val openSubscriptions = CompositeDisposable()
    private var articles: List<OptionalArticle> = emptyList()


    fun loadArticleFeed(feedType: Feed) {
        // we're creating a mutable list to cache our articles from doOnNext so we only have to
        // overwrite the value of 'articles' once when all the data is present instead of having
        // to create a new list instance for every onNext event
        val articleCache = mutableListOf<OptionalArticle>()
        val observableArticles = loadAggregateArticleFeed(feedType)
                .flatMap { networkResult ->
                    networkResult.toOptionalArticleAggregate().toObservableArticleIds()
                }
                .flatMap { articleId -> articleService.loadArticle(articleId) }
                .flatMap { networkResult -> networkResult.toOptionalArticle() }
                .doOnSubscribe { articles = emptyList() }
                // append new articles to the cache list
                .doOnNext({ optionalArticle -> articleCache.add(optionalArticle) })
                // replace our immutable article store with the cache once when we have all the data
                .doOnComplete({ articles = articleCache.toList() })
        openSubscriptions.add(observableArticles.defaultSubscribe())
    }

    private fun loadAggregateArticleFeed(feedType: Feed): Observable<NetworkResult> {
        return when (feedType) {
            is Feed.NewStories -> aggregateService.loadNewStories()
            is Feed.BestStories -> aggregateService.loadBestStories()
            is Feed.TopStories -> aggregateService.loadTopStories()
        }
    }

    private fun NetworkResult.toOptionalArticle(): Observable<OptionalArticle> {
        val optionalArticle = when (this) {
            is HttpError -> NoArticle // TODO: handle error
            is Payload -> (this as Payload.SingleArticle).article
        }
        return Observable.just(optionalArticle)
    }


    private fun NetworkResult.toOptionalArticleAggregate() =
            when (this) {
                is HttpError -> NoArticles // TODO: handle error
                is Payload -> (this as Payload.AggregateArticles).articleAggregate
            }


    private fun OptionalArticleAggregate.toObservableArticleIds(): Observable<Long> {
        val articleIds = when (this) {
            is NoArticles -> emptyList<Long>()
            is ArticleAggregate -> this.articleIds
        }
        return Observable.fromIterable(articleIds)
    }

    private fun Observable<OptionalArticle>.defaultSubscribe(): Disposable {
        return subscribe({ /* nothing on onNext */ },
                { Log.e("FeedViewModel", it.message) },
                { Log.d("FeedViewModel", "Fetched ${articles.size} articles.") })
    }

    override fun onCleared() {
        openSubscriptions.clear()
        super.onCleared()
    }

}

sealed class Feed {
    object NewStories : Feed()
    object BestStories : Feed()
    object TopStories : Feed()
}

