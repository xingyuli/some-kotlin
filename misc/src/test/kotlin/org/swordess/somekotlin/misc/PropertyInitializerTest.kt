package org.swordess.somekotlin.misc

import org.junit.Test
import kotlin.properties.Delegates

class PropertyInitializerTest {

    private val a = 1
    private val str = "abc"

    private lateinit var lateinitVariable: String

    private var notNullVariable by Delegates.notNull<String>()

    @Test
    fun test() {
        lateinitVariable = "assign value to lateinit variable"
        notNullVariable = "assign value to notNull variable"
    }

}