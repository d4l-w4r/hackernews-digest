package de.walled.hackernewsdigest.networking

import de.walled.hackernewsdigest.data.OptionalArticle
import de.walled.hackernewsdigest.data.OptionalArticleAggregate

sealed class NetworkResult {

    companion object {
        fun fromAggregateResponse(payload: OptionalArticleAggregate): NetworkResult {
            return Payload.AggregateArticles(payload)
        }

        fun fromArticleResponse(payload: OptionalArticle): NetworkResult {
            return Payload.SingleArticle(payload)
        }

        fun fromErrorResponse(code: Int, message: String?): NetworkResult {
            return when (code) {
                404 -> HttpError.ResourceNotFound
                503 -> HttpError.ServiceUnavailable
                else -> HttpError.UnknownError(code, message ?: "")
            }
        }
    }
}

sealed class Payload : NetworkResult() {

    data class AggregateArticles(val articleAggregate: OptionalArticleAggregate) : Payload()

    data class SingleArticle(val article: OptionalArticle) : Payload()
}

sealed class HttpError : NetworkResult() {

    object ResourceNotFound : HttpError()

    object ServiceUnavailable : HttpError()

    data class UnknownError(val code: Int, val message: String) : HttpError()
}