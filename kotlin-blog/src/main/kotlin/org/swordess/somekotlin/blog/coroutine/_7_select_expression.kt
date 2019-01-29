package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlin.random.Random

fun main(args: Array<String>) {
    // selectingFromChannels()
    // selectingOnClose()
    // selectingToSend()
    // selectingDeferredValues()
    switchOverAchannelOfDeferredValues()
}

/* *********************************** */
/* ***** Selecting from channels ***** */
/* *********************************** */

private fun selectingFromChannels() = runBlocking<Unit> {
    val fizz = fizz()
    val buzz = buzz()
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren()
}

// sends "Fizz" every 300 ms
private fun CoroutineScope.fizz() = produce<String> {
    while (true) {
        delay(300)
        send("Fizz")
    }
}

// sends "Buzz!" every 500 ms
private fun CoroutineScope.buzz() = produce<String> {
    while (true) {
        delay(500)
        send("Buzz!")
    }
}

private suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    // <Unit> means that this select expression does not produce any result
    select<Unit> {
        // this is the first select clause
        fizz.onReceive { value ->
            println("fizz -> '$value'")
        }
        // this is the second select clause
        buzz.onReceive { value ->
            println("buzz -> '$value'")
        }
    }
}

/* ****************************** */
/* ***** Selecting on close ***** */
/* ****************************** */

private fun selectingOnClose() = runBlocking<Unit> {
    val a = produce {
        repeat(4) {
            send("Hello $it")
        }
    }
    val b = produce {
        repeat(4) {
            send("World $it")
        }
    }
    // print first eight results
    repeat(8) {
        println(selectAorB(a, b))
    }
    coroutineContext.cancelChildren()
}

private suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
    select<String> {
        a.onReceiveOrNull { value ->
            if (value == null) "Channel 'a' is closed" else "a -> '$value'"
        }
        b.onReceiveOrNull { value ->
            if (value == null) "Channel 'b' is closed" else "b -> '$value'"
        }
    }

/* *********************** ***** */
/* ***** Selecting to send ***** */
/* *********************** ***** */

private fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
    for (num in 1..10) { // produce 10 numbers from 1 to 10
        delay(100) // every 100 ms
        select<Unit> {
            // send to the primary channel
            this@produce.onSend(num) {

            }

            // or to the side channel
            side.onSend(num) {

            }
        }
    }
}

private fun selectingToSend() = runBlocking {
    val side = Channel<Int>()
    // this is a very fast consumer of the side channel
    launch {
        side.consumeEach { println("Side channel has $it") }
    }
    produceNumbers(side).consumeEach {
        println("Consuming $it")
        delay(250) // let us digest the consumed number properly, do not hurry
    }
    println("Done consuming")
    coroutineContext.cancelChildren()
}

/* ************************************ */
/* ***** Selecting deferred value ***** */
/* ************************************ */

private fun CoroutineScope.asyncString(time: Int) = async {
    delay(time.toLong())
    "Waited for $time ms"
}

private fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
    val random = Random(3)
    return List(12) { asyncString(random.nextInt(1000)) }
}

private fun selectingDeferredValues() = runBlocking {
    val list = asyncStringsList()
    val result = select<String> {
        list.forEachIndexed { index, deferred ->
            deferred.onAwait { answer ->
                "Deferred $index produced answer '$answer'"
            }
        }
    }
    println(result)
    val countActive = list.count { it.isActive }
    println("$countActive coroutines are still alive")
}

/* **************************************************** */
/* ***** Switch over a channel of deferred values ***** */
/* **************************************************** */

private fun CoroutineScope.switchMapDeferred(input: ReceiveChannel<Deferred<String>>): ReceiveChannel<String> =
    produce<String> {
        var current: Deferred<String> = input.receive() // start with first received deferred value
        while (isActive) { // loop while not cancelled/closed
            // return next deferred value from this select or null
            val next = select<Deferred<String>?> {
                input.onReceiveOrNull { update ->
                    update // replaces next value to wait
                }
                current.onAwait { value ->
                    send(value) // send value that current deferred has produced
                    input.receiveOrNull() // and use the next deferred from the input channel
                }
            }

            if (next == null) {
                log("Channel was closed")
                break
            } else {
                current = next
            }
        }
    }

private var start: Long? = null
private fun log(msg: String) = println("${System.currentTimeMillis() - start!!} ${Thread.currentThread()}: $msg")

private suspend fun doDelay(time: Long) {
    // log("delay $time ms")
    delay(time)
}

private fun CoroutineScope.asyncString(str: String, time: Long) = async(CoroutineName("producer($str)")) {
    doDelay(time)
    log("produce $str")
    str
}

private fun switchOverAchannelOfDeferredValues() = runBlocking(CoroutineName("runBlocking")) {
    val chan = Channel<Deferred<String>>() // the channel for test

    // launch printing coroutine
    launch(CoroutineName("consumer")) {
        for (s in switchMapDeferred(chan)) {
            log(s)
        }
    }

    start = System.currentTimeMillis()
    // TIMELINE: 0

    chan.send(asyncString("BEGIN", 100)) // will be produced at 100
    doDelay(200) // enough time for "BEGIN" to be produced

    // "BEGIN" produced at 100

    // TIMELINE: 200

    chan.send(asyncString("Slow", 500)) // will be produced at 700
    doDelay(100) // not enough time to produce slow

    // TIMELINE: 300

    chan.send(asyncString("Replace", 100)) // will be produced at 400
    doDelay(500) // give it time before the last one

    // "Replace" produced at 400
    // "Slow" produced at 700

    // TIMELINE: 800

    chan.send(asyncString("END", 500)) // will be produced at 1300
    doDelay(1000) // give it time to process

    // "END" produced at 1300

    // NOW: 1800

    chan.close() // close the channel ...
    doDelay(500) // and wait some time to let it finish

    // TIMELINE: 2300

    log("Done")
}
