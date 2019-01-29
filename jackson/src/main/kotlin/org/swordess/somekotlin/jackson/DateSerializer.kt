package org.swordess.somekotlin.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.text.SimpleDateFormat
import java.util.Date

class DateSerializer : StdSerializer<Date>(Date::class.java) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun serialize(value: Date, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString(dateFormat.format(value))
    }

}