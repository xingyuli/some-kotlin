package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    // volatilesAreOfNoHelp()
    // threadSafeDataStructures()
    // threadConfinementFineGrained()
    // threadConfinementCoarseGrained()
    // mutualExclusion()
    usingActors()
}

private suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
    val n = 100 // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}

/* *********************************************** */
/* ***** Solution 1. @Volatile - not working ***** */
/* *********************************************** */

@Volatile // in kotlin `volatile` is an annotation
var counter1 = 0

private suspend fun volatilesAreOfNoHelp() {
    GlobalScope.massiveRun {
        counter1++
    }
    println("Counter = $counter1")
}

/* *************************************************** */
/* ***** Solution 2. Thread-safe data structures ***** */
/* *************************************************** */

private suspend fun threadSafeDataStructures() {
    val counter = AtomicInteger()

    GlobalScope.massiveRun {
        counter.incrementAndGet()
    }
    println("Counter = $counter")
}

/* ******************************************************* */
/* ***** Solution 3. Thread confinement fine-grained ***** */
/* ******************************************************* */

private suspend fun threadConfinementFineGrained() {
    val counterContext = newSingleThreadContext("CounterContext")
    var counter = 0

    GlobalScope.massiveRun { // run each coroutine with DefaultDispatcher
        withContext(counterContext) { // but confine each increment to the single-threaded context
            counter++
        }
    }
    println("Counter = $counter")
}

/* ********************************************************* */
/* ***** Solution 4. Thread confinement coarse-grained ***** */
/* ********************************************************* */

private suspend fun threadConfinementCoarseGrained() {
    val counterContext = newSingleThreadContext("CounterContext")
    var counter = 0

    CoroutineScope(counterContext).massiveRun { // run each coroutine in the single-threaded context
        counter++
    }
    println("Counter = $counter")
}

/* **************************************** */
/* ***** Solution 5. Mutual exclusion ***** */
/* **************************************** */

private suspend fun mutualExclusion() {
    val mutex = Mutex()
    var counter = 0

    // The locking for this example is fine-grained, so it pays the price.
    // However, it is a good choice for some situations where you absolutely
    // must modify some shared state periodically, but there is no natural
    // thread that this state is confined to.
    GlobalScope.massiveRun {
        mutex.withLock {
            counter++
        }
    }
    println("Counter = $counter")
}

/* ****************************** */
/* ***** Solution 6. Actors ***** */
/* ****************************** */

// The first step of using an actor is to define a class of messages that an
// actor is going to process. Kotlin's `sealed classes` are well suited for
// that purpose.

// Message types for counterActor
sealed class CounterMsg
object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply

// This function launches a new counter actor
private fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

private suspend fun CoroutineScope.usingActors() {
    val counter = counterActor() // create the actor
    GlobalScope.massiveRun {
        counter.send(IncCounter)
    }
    // send a message to get a counter value from an actor
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println("Counter = ${response.await()}")
    counter.close() // shutdown the actor
}
