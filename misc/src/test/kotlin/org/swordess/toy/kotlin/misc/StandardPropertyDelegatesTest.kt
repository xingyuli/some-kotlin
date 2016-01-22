package org.swordess.toy.kotlin.misc

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

class StandardPropertyDelegatesTest {

    /* Standard Delegate: lazy, called if not initialized yet */

    private var nameInitialized = false
    private val name: String by lazy {
        nameInitialized = true
        "vic"
    }

    @Test
    fun testLazy() {
        assertFalse(nameInitialized)
        name
        assertTrue(nameInitialized)
    }



    /* Standard Delegate: observable, called after changes */

    private var exceedThresholdLimit = AtomicInteger()
    private var threshold: Int by Delegates.observable(0) { prop, old, new ->
        if (new > 1000) {
            exceedThresholdLimit.incrementAndGet()
        }
    }

    @Test
    fun testObservable() {
        val history = ArrayList<Int>()
        arrayOf(200, 1200, 300, 1300).forEach {
            threshold = it
            history.add(threshold)
        }
        assertEquals(2, exceedThresholdLimit.get())
        assertArrayEquals(arrayOf(200, 1200, 300, 1300), history.toArray())
    }



    /* Standard Delegate: vetoable, called before changes */

    private var threshold2: Int by Delegates.vetoable(0) { prop, old, new ->
        new <= 1000
    }

    @Test
    fun testVetoable() {
        val history = ArrayList<Int>()
        arrayOf(200, 1200, 300, 1300).forEach {
            threshold2 = it
            history.add(threshold2)
        }
        assertArrayEquals(arrayOf(200, 200, 300, 300), history.toArray())
    }



    /* Standard Delegate: notNull */

    private var assumedNotNull: String by Delegates.notNull<String>()

    @Test
    fun testNotNull() {
        try {
            assumedNotNull
            fail("should not happen")
        } catch (e: IllegalStateException) {
            assertEquals("Property assumedNotNull should be initialized before get.", e.message)
        }

        assumedNotNull = "has value now"
        try {
            assumedNotNull
        } catch (e: Exception) {
            fail("should not happen")
        }
    }



    /* Standard Delegate: map */

    private val backingMap = HashMap<String, Any>()
    private var timestamp: Date by backingMap

    @Test
    fun testMap() {
        assertTrue(backingMap.isEmpty())
        timestamp = Date()
        assertTrue("timestamp" in backingMap && timestamp === backingMap["timestamp"])
    }

}