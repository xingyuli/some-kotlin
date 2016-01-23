package org.swordess.toy.kotlin.misc

import org.junit.Assert
import org.junit.Test
import java.util.HashMap

class MapTest {

    @Test
    fun test() {
        // this is a Kotlin Map
        val map: Map<String, String> = HashMap()

        // not possible to put values into the map as the Map is immutable
        // map["one"] = 1

        // modifications can only happened to MutableMap
        val m: MutableMap<String, String> = map as MutableMap<String, String>
        m["one"] = "1"
        m += "two" to "2"

        Assert.assertTrue("one" in m)
        Assert.assertTrue("two" in m)

        // and there is no magic when assigning java.util.HashMap to the Kotlin MutableMap
        Assert.assertTrue(map === m)
    }

}