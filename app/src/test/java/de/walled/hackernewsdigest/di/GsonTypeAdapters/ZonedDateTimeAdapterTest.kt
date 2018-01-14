package de.walled.hackernewsdigest.di.GsonTypeAdapters

import com.google.gson.JsonPrimitive
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class ZonedDateTimeAdapterTest {

    @Test
    fun `When deserializing a Unix time timestamp it will be correctly converted to a ZonedDateTime object with UTC time zone `() {
        val unixTimeString = "1515682653"
        val expectedZonedDateTime = ZonedDateTime.of(2018, 1, 11, 14, 57, 33, 0, ZoneId.of("UTC"))
        val adapter = ZonedDateTimeAdapter()
        val deserializedTime = adapter.deserialize(JsonPrimitive(unixTimeString), ZonedDateTime::class.java, null)
        assertTrue(deserializedTime == expectedZonedDateTime)
    }

    @Test
    fun `When serializing a ZonedDateTime object with UTC time zone it will be correctly converted to a Unix time timestamp Long`() {
        val expectedUnixTimeString = "1515682653"
        val zonedDateTime = ZonedDateTime.of(2018, 1, 11, 14, 57, 33, 0, ZoneId.of("UTC"))
        val adapter = ZonedDateTimeAdapter()
        val serializedTime = adapter.serialize(zonedDateTime, ZonedDateTime::class.java, null)
        assertTrue(serializedTime.toString() == expectedUnixTimeString)
    }
}