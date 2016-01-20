package org.swordess.toy.kotlin.misc.delegation

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