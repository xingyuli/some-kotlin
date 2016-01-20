package org.swordess.toy.kotlin.misc.delegation

import org.junit.Assert
import org.junit.Test

class DelegationTest {

    @Test
    fun testPartialDelegation() {
        val delegated = RealMachine()
        val m = MonitoredMachine(delegated)
        Assert.assertEquals("[monitored] ${delegated.run()}", m.run())
        Assert.assertEquals("${delegated.start()}", m.start())
    }

}