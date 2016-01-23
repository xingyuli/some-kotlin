package org.swordess.toy.kotlin.misc

import org.junit.Assert
import org.junit.Test

class UseSingletonInKotlinTest {

    @Test
    fun testCompanionSingleton() {
        val first = CompanionSingleton.instance
        val second = CompanionSingleton.instance
        Assert.assertSame(first, second)
    }

    @Test
    fun testStandardSingleton() {
        val first = StandardSingleton.getInstance()
        val second = StandardSingleton.getInstance()
        Assert.assertSame(first, second)
    }

    @Test
    fun testShorthandSingleton() {
        // only access INSTANCE triggers the initialization
        println(ShorthandSingleton::class)
        println(ShorthandSingleton) // print: do some preparation here

        val first = ShorthandSingleton
        val second = ShorthandSingleton
        Assert.assertSame(first, second)
    }

}
