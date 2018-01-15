package de.walled.hackernewsdigest

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import de.walled.hackernewsdigest.networking.NewsAggregateService
import de.walled.hackernewsdigest.networking.NewsArticleService
import de.walled.hackernewsdigest.newsfeed.Feed
import de.walled.hackernewsdigest.newsfeed.FeedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var aggregateService: NewsAggregateService

    @Inject
    lateinit var articleService: NewsArticleService

    private lateinit var viewModel: FeedViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                viewModel.loadArticleFeed(Feed.NewStories)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                viewModel.loadArticleFeed(Feed.BestStories)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                viewModel.loadArticleFeed(Feed.TopStories)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HackerNewsApp.hackerNewsComponent.inject(this)
        setContentView(R.layout.activity_main)
        viewModel = FeedViewModel(aggregateService, articleService)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}
