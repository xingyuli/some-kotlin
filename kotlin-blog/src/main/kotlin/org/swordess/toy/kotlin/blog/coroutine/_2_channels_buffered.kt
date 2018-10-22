package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = Channel<Int>(4) // create buffered channel

    // launch sender coroutine
    val sender = launch {
        repeat(10) {
            println("Sending $it") // print before sending each element
            channel.send(it) // will suspend when buffer is full
            println("Sent $it")
        }
    }

    // don't receive anything... just wait...
    delay(1000)
    sender.cancel() // cancel sender coroutine
}