package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Multiple coroutines may send to the same channel.
fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = Channel<String>()
    launch { sendString(channel, "foo", 200) }
    launch { sendString(channel, "BAR!", 500) }

    // receive first six
    repeat(6) {
        println(channel.receive())
    }

    coroutineContext.cancelChildren() // cancel all children to let main finish
}

private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}
