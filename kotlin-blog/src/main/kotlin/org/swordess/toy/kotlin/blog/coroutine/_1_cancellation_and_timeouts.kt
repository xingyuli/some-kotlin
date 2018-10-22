package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    // cancellingCoroutineExecution()
    // cancellationIsCooperative()
    // makingComputationCodeCancellable()
    // closingResourcesWithFinally()
    timeout()
}

private suspend fun CoroutineScope.cancellingCoroutineExecution() {
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
    delay(1300) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    println("main: Now I can quit.")
}

// All the suspending functions in `kotlinx.coroutines` are cancellable. They
// check for cancellation of coroutine and throw `CancellationException` when
// cancelled. However, if a coroutine is working in a computation and does not
// check for cancellation, then it cannot be cancelled.
private suspend fun CoroutineScope.cancellationIsCooperative() {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // computation loop, just wastes CPU
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun CoroutineScope.makingComputationCodeCancellable() {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // cancellable computation loop
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

private suspend fun CoroutineScope.closingResourcesWithFinally() {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500)
            }
        } finally {
            println("I'm running finally")
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for completion
    println("main: Now I can quit.")
}

private suspend fun timeout() {
    withTimeout(1300) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}