package de.walled.hackernewsdigest.di.GsonTypeAdapters

import com.google.gson.*
import org.threeten.bp.Instant
import org.threeten.bp.ZonedDateTime
import java.lang.reflect.Type


class ZonedDateTimeAdapter : JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    override fun serialize(src: ZonedDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        return src?.let {
            JsonPrimitive(it.toEpochSecond())
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ZonedDateTime? {
        return json?.asLong?.let {
            ZonedDateTime.from(Instant.ofEpochSecond(it))
        }
    }
}