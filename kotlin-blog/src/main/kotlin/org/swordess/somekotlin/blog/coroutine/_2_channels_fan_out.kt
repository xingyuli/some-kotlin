package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Multiple coroutines may receive from the same channel, distributing work
// between themselves.
fun main(args: Array<String>) = runBlocking<Unit> {
    val producer = produceNumbers()
    repeat(5) { launchProcessor(it, producer) }
    delay(950)

    // Note, that cancelling a producer coroutine closes its channel, thus
    // eventually terminating iteration over the channel that processor
    // coroutines are doing.
    producer.cancel() // cancel producer coroutine and thus kill them all
}

private fun CoroutineScope.produceNumbers() = produce {
    var x = 1
    while (true) {
        send(x++)
        delay(100)
    }
}

private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println("Processor $id received $msg")
    }
}
