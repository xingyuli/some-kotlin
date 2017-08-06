package org.swordess.toy.kotlin.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SnakeCaseTest {

    private lateinit var mapper: ObjectMapper

    @Before
    fun setUp() {
        mapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES
        }
    }

    @Test
    fun testDeserialization() {
        val project = mapper.readValue("""{
"first_name": "Vic",
"last_name": "Lau",
"gender": "Male"
}""", Project::class.java)
        Assert.assertEquals(project.firstName, "Vic")
        Assert.assertEquals(project.lastName, "Lau")
        Assert.assertEquals(project.gender, "Male")
    }

    @Test
    fun testSerialization() {
        val project = Project().apply {
            firstName = "Vic"
            lastName = "Lau"
            gender = "Male"
        }
        val projectJson = mapper.writeValueAsString(project)
        Assert.assertEquals(projectJson, """{"first_name":"Vic","last_name":"Lau","gender":"Male"}""")
    }

    class Project {
        var firstName: String? = null
        var lastName: String? = null
        var gender: String? = null
    }

}