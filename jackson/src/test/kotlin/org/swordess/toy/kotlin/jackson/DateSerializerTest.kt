package org.swordess.toy.kotlin.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class DateSerializerTest {

    private lateinit var mapper: ObjectMapper

    @Before
    fun setUp() {
        val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, "my-app", "app-layer"))
        testModule.addSerializer(DateSerializer())

        mapper = ObjectMapper().registerModule(testModule)
    }

    @Test
    fun testSerializeSingleDate() {
        Assert.assertEquals("\"2015-11-25 00:00:00\"", mapper.writeValueAsString(getTestDate()))
    }

    @Test
    fun testSerializeMapWithDate() {
        val expected = """{"now":"2015-11-25 00:00:00"}"""
        Assert.assertEquals(expected, mapper.writeValueAsString(mapOf("now" to getTestDate())))
    }

    @Test
    fun testSerializeBeanWithDate() {
        val expected = """{"now":"2015-11-25 00:00:00"}"""
        Assert.assertEquals(expected, mapper.writeValueAsString(MyBean(getTestDate())))
    }

    class MyBean(val now: Date)

    companion object {

        private const val DATE_STR = "2015-11-25"

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun getTestDate(): Date = dateFormat.parse(DATE_STR)

    }

}