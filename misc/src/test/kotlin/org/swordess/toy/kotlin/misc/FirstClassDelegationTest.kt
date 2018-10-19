package org.swordess.toy.kotlin.misc

import org.junit.Assert.assertEquals
import org.junit.Test

interface Machine {
    fun start(): String
    fun run(): String
    fun stop(): String
}

class RealMachine : Machine {
    override fun start(): String = "${javaClass.simpleName} start"
    override fun run(): String = "${javaClass.simpleName} run"
    override fun stop(): String = "${javaClass.simpleName} stop"
}

class MonitoredMachine(private val m: Machine) : Machine by m {
    override fun run(): String = "[monitored] " + m.run()
}

class FirstClassDelegationTest {

    @Test
    fun testPartialDelegation() {
        val delegated = RealMachine()
        val m = MonitoredMachine(delegated)
        assertEquals("[monitored] ${delegated.run()}", m.run())
        assertEquals(delegated.start(), m.start())
    }

}