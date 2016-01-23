package org.swordess.toy.kotlin.misc

import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class JavaClassAndKClassTest {

    private val bean = JavaBean()

    @Test
    fun test() {
        val kClass = JavaBean::class
        Assert.assertTrue(kClass is KClass)
        Assert.assertTrue(kClass.java is java.lang.Class)

        val javaClass = bean.javaClass
        Assert.assertTrue(javaClass is java.lang.Class)
        Assert.assertTrue(javaClass.kotlin is KClass)
    }

}