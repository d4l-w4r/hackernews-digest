package de.walled.hackernewsdigest.data

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class HackerNewsItem(
        val id: Long,
        val type: ItemType? = null,
        var deleted: Boolean? = null,
        var by: String? = null,
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
)

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