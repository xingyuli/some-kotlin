package org.swordess.somekotlin.blog.libchange

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

@UseExperimental(ExperimentalTime::class)
suspend fun main() {
    greetAfterTimeout(100.milliseconds)
    greetAfterTimeout(1.seconds)
}

@ExperimentalTime
suspend fun greetAfterTimeout(duration: Duration) {
    delay(duration.toLongMilliseconds())
    println("Hi!")
}
