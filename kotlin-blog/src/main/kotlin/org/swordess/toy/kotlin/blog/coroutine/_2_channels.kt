package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    // basics()
    // closingAndIterationOverChannels()
    // consumeSquares()
    // consumeNumbers()
    generatePrimes()
}

// A `Channel` is conceptually very similar to `BlockingQueue`. One key
// difference is that
//   instead of a blocking `put` operation it has a suspending `send`, and
//   instead of a blocking `take` operation it has a suspending `receive`.
private fun basics() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        // this might be heavy CPU-consuming computation or async logic, we'll
        // just send five squares
        for (x in 1..5) channel.send(x * x)
    }
    // here we print five received integers
    repeat(5) {
        println(channel.receive())
    }
    println("Done!")
}

// Unlike a queue, a channel can be closed to indicate that no more elements
// are coming. On the receiver side it is convenient to use a regular `for`
// loop to receive elements from the channel.
//
// Conceptually, a close is like sending a special close token to the channel.
// The iteration stops as soon as this close token is received, so there is a
// guarantee that all previously sent elements before the close are received.
private fun closingAndIterationOverChannels() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // we're done sending
    }
    // here we print received values using `for` loop (until the channel is closed)
    for (y in channel) {
        println(y)
    }
    println("Done!")
}

/* ***** BuildingChannelProducers ***** */

private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) {
        send(x * x)
    }
}

private fun consumeSquares() = runBlocking<Unit> {
    val squares = produceSquares()
    squares.consumeEach {
        println(it)
    }
    println("Done!")
}

/* ***** Pipelines ***** */

private fun CoroutineScope.produceNumbers(): ReceiveChannel<Int> = produce {
    var x = 1
    // infinite stream of integers starting from 1
    while (true) {
        send(x++)
    }
}

private fun CoroutineScope.square(numbers: ReceiveChannel<Int>) = produce {
    for (x in numbers) {
        send(x * x)
    }
}

private fun consumeNumbers() = runBlocking<Unit> {
    val numbers = produceNumbers() // produces integers from 1 and on
    val squares = square(numbers) // squares integers
    // print first five
    for (i in 1..5) {
        println(squares.receive())
    }
    println("Done!") // we are done
    coroutineContext.cancelChildren() // cancel children coroutines
}

/* ***** Prime numbers with pipeline ***** */

private fun CoroutineScope.numbersFrom(start: Int) = produce {
    var x = start
    // infinite stream of integers from start
    while (true) {
        send(x++)
    }
}

private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (x in numbers) {
        if (x % prime != 0) {
            send(x)
        }
    }
}

private fun generatePrimes() = runBlocking<Unit> {
    var cur = numbersFrom(2)
    for (i in 1..10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren() // cancel all children to let main finish
}
