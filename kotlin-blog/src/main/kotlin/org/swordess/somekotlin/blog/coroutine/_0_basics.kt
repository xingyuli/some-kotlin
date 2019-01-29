package org.swordess.somekotlin.blog.coroutine

import kotlinx.coroutines.*
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    firstCoroutine()
    bridgingBlockingAndNoBlockingWorlds()
    waitingForAJob()
    structuredConcurrency()
    scopeBuilder()
    extractFunctionRefactoring()
    coroutinesAreLightWeight()
    globalCoroutinesAreLikeDaemonThreads()
}

private fun firstCoroutine() {
    // launch new coroutine in background and continue
    GlobalScope.launch {
        delay(1000) // non-blocking delay for 1 second
        println("World!") // print after delay
    }

    // You can achieve the same result with:
//    thread {
//        Thread.sleep(1000)
//        println("World!")
//    }

    println("Hello,") // main thread continues while coroutine is delayed
    Thread.sleep(2000) // block main thread for 2 seconds to keep JVM alive
}

private fun bridgingBlockingAndNoBlockingWorlds() {
    GlobalScope.launch {
        delay(1000)
        println("World!")
    }
    println("Hello,")
    runBlocking {
        delay(2000)
    }
}

private fun waitingForAJob() = runBlocking<Unit> {
    val job = GlobalScope.launch {
        delay(1000)
        println("World!")
    }
    println("Hello,")
    job.join() // wait unit child coroutine completes
}

private fun structuredConcurrency() = runBlocking<Unit> {
    // launch new coroutine in the scope of runBlocking
    launch {
        delay(1000)
        println("World!")
    }
    println("Hello,")
}

private fun scopeBuilder() = runBlocking<Unit> {
    launch {
        delay(200)
        println("Task from runBlocking")
    }

    coroutineScope {
        launch {
            delay(500)
            println("Task from nested launch")
        }
        delay(100)
        println("Task from coroutine scope")
    }

    println("Coroutine scope is over")
}

private fun extractFunctionRefactoring() = runBlocking<Unit> {
    launch { doWorld() }
    println("Hello,")
}

// When you perform 'Extract function' refactoring on this code you get a new
// function with `suspend` modifier
private suspend fun doWorld() {
    delay(1000)
    println("World!")
}

private fun coroutinesAreLightWeight() = runBlocking<Unit> {
    repeat(100_000) {
        // launch a lot of coroutines
        launch {
            delay(1000)
            print(".")
        }
    }
}

private fun globalCoroutinesAreLikeDaemonThreads() = runBlocking<Unit> {
    // active coroutines that were launched in `GlobalScope` do not keep the process alive
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300)
}