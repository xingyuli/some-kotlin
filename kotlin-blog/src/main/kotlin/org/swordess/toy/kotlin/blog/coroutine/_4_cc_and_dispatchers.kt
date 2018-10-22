package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.*

fun main(args: Array<String>) {
    // dispatchersAndThreads()
    // jobInTheContext()
    // childrenOfACoroutine()
    // parentalResponsibilities()
    // namingCoroutinesForDebugging()
    threadLocalData()
}

private fun dispatchersAndThreads() = runBlocking<Unit> {
    // context of the parent, main runBlocking coroutine
    launch {
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }

    // not confined -- will work with main thread
    launch(Dispatchers.Unconfined) {
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }

    // will get dispatched to DefaultDispatcher
    launch(Dispatchers.Default) {
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }

    // will get its own new thread
    launch(newSingleThreadContext("MyOwnThread")) {
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
}

private fun jobInTheContext() = runBlocking<Unit> {
    println("My job is ${coroutineContext[Job]}")
}

private fun childrenOfACoroutine() = runBlocking<Unit> {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        // it spawns two other jobs, one with GlobalScope
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        // and the other inherits the parent context
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }

    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    println("main: Who has survived request cancellation?")
}

// A parent coroutine always waits for completion of all its children. Parent
// does not have to explicitly track all the children it launches and it does
// not have to use `Job.join` to wait for them at the end.
private fun parentalResponsibilities() = runBlocking<Unit> {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        repeat(3) { i -> // launch a few children jobs
            launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // wait for completion of the request, including all its children
    println("Now processing of the request is complete")
}

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

// Note: debugging mode should be turned on via '-Dkotlinx.coroutines.debug'
private fun namingCoroutinesForDebugging() = runBlocking<Unit> {
    log("Started main coroutine")

    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        log("Computing v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        log("Computing v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
}

/* ***************************** */
/* ***** Thread-local data ***** */
/* ***************************** */

private val threadLocal = ThreadLocal<String?>() // declare thread-local variable

private fun threadLocalData() = runBlocking<Unit> {
    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

    val job = launch(Dispatchers.Default + threadLocal.asContextElement("launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    job.join()

    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
}
