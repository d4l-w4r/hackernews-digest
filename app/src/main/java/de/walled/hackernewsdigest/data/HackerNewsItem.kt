package de.walled.hackernewsdigest.data

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import de.walled.hackernewsdigest.di.GsonTypeAdapters.ZonedDateTimeAdapter
import org.threeten.bp.ZonedDateTime

sealed class OptionalArticle

enum class ItemType {
    @SerializedName("job")
    JOB,
    @SerializedName("story")
    STORY,
    @SerializedName("comment")
    COMMENT,
    @SerializedName("poll")
    POLL,
    @SerializedName("pollopt")
    POLL_OPT
}

data class HackerNewsItem(
        val id: Long,
        val type: ItemType? = null,
        var deleted: Boolean? = null,
        var by: String? = null,
        @JsonAdapter(ZonedDateTimeAdapter::class)
        var time: ZonedDateTime? = null,
        var text: String? = null,
        var dead: Boolean? = null,
        var parent: Long? = null,
        var poll: Long? = null,
        var kids: List<Long>? = null,
        var url: String? = null,
        var score: Int? = null,
        var title: String? = null,
        var parts: List<Long>? = null,
        var descendants: Int? = null
) : OptionalArticle()

object NoArticle : OptionalArticle()