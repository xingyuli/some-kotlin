package org.swordess.somekotlin.blog.libchange

import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock
import kotlin.time.measureTimedValue

@UseExperimental(ExperimentalTime::class)
fun main() {
    useClockMark()
    useMeasureTimedValue()
}

@ExperimentalTime
private fun useClockMark() {
    val clock = MonoClock
    val mark = clock.markNow() // might be inside the first function
    Thread.sleep(10)    // action
    println(mark.elapsedNow()) // might be inside the second function
}

@ExperimentalTime
private fun useMeasureTimedValue() {
    val (value, duration) = measureTimedValue {
        Thread.sleep(100)
        42
    }
    println(value)
    println(duration)
}
