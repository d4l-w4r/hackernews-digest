package de.walled.hackernewsdigest.data


sealed class OptionalArticleAggregate

data class ArticleAggregate(val articleIds: List<Long>) : OptionalArticleAggregate()

object NoArticles : OptionalArticleAggregate()