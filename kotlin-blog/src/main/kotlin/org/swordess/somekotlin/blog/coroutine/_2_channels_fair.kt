package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Send and receive operations to channels are `fair` with respect to the order
// their invocation from multiple coroutines. They are served in first-in
// first-out order.
fun main(args: Array<String>) = runBlocking<Unit> {
    val table = Channel<Ball>() // a shared table

    // The 'ping' coroutine is started first, so it is the first to receive the ball.
    launch { player("ping", table) }
    launch { player("pong", table) }

    table.send(Ball(0)) // serve the ball
    delay(1000) // delay 1 second
    coroutineContext.cancelChildren() // game over, cancel them
}

private data class Ball(var hits: Int)

private suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) { // receive the ball in a loop
        ball.hits++
        println("$name $ball")
        delay(300) // wait a bit
        table.send(ball) // send the ball back
    }
}