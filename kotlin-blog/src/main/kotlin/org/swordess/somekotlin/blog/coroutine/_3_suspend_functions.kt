package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
//    sequentialByDefault()
//    concurrentUsingAsync()
//    lazilyStartedAsync()
//    asyncStyleFunction()
//    structuredConcurrencyWithAsync()
    demoFailedConcurrentSum()
}

private suspend fun doSomethingUsefulOne(): Int {
    delay(1000) // pretend we are doing something useful here
    return 13
}

private suspend fun doSomethingUsefulTwo(): Int {
    delay(1000) // pretend we are doing something useful here, too
    return 29
}

private fun sequentialByDefault() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms") // 2034 ms
}

private fun concurrentUsingAsync() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms") // 1012 ms
}

private fun lazilyStartedAsync() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }

        // some computation

        one.start() // start the first one
        two.start() // start the second one
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/* ********************************* */
/* ***** Async-style functions ***** */
/* ********************************* */

private fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}

private fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}

// This programming style with async functions is provided here only for
// illustration, because it is a popular style in other programming languages.
// Using this style with Kotlin coroutines is strongly discouraged for the
// reasons that are explained below.
private fun asyncStyleFunction() {
    val time = measureTimeMillis {
        // we can initiate async actions outside of a coroutine
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()

        // but waiting for a result must involve either suspending or blocking.
        // here we use `runBlocking { ... }` to block the main thread while awaiting for the result
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time ms")
}

/* ********************************************* */
/* ***** Structured concurrency with async ***** */
/* ********************************************* */

private suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

// If something goes wrong inside the code of `concurrentSum` function and it
// throws an exception, all the coroutines that were launched in its scope are
// cancelled.
private fun structuredConcurrencyWithAsync() = runBlocking<Unit> {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}

private suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE)
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

private fun demoFailedConcurrentSum() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}
