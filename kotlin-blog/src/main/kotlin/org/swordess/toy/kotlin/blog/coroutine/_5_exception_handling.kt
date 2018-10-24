package org.swordess.toy.kotlin.blog.coroutine

import kotlinx.coroutines.*
import java.io.IOException
import java.lang.ArithmeticException
import java.lang.IndexOutOfBoundsException

fun main(args: Array<String>) {
    // exceptionPropagation()
    // coroutineExceptionHandler()
    // cancellationAndExceptions_cancelWithoutCause()
    // cancellationAndExceptions_cancelWithCause()
    exceptionsAggregation()
}

// Coroutine builders come in two flavors:
//   - propagating exceptions automatically(`launch` and `actor`) or
//   - exposing them to users(`async` and `produce`)
//
// The former treat exceptions as unhandled, similar to Java's
// `Thread.uncaughtExceptionHandler`, while the later are relying on the users
// to consume the final exception.
private fun exceptionPropagation() = runBlocking {
    val job = GlobalScope.launch {
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
    }
    job.join()
    println("Joined failed job")

    val deferred = GlobalScope.async {
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }
    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}

private fun coroutineExceptionHandler() = runBlocking {
    // `CoroutineExceptionHandler` is invoked on exceptions which are not
    // expected to be handled by the user, so registering it in `async` builder
    // and the like of it has no effect.
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }
    val deferred = GlobalScope.async(handler) {
        throw ArithmeticException() // Nothing will be printed, relying on user to call deferred.await()
    }
    joinAll(job, deferred)
}

// When a coroutine is cancelled using `Job.cancel` without a cause, it
// terminates, but it does not cancel its parent.
private fun cancellationAndExceptions_cancelWithoutCause() = runBlocking {
    val job = launch {
        val child = launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Child is cancelled")
            }
        }
        yield()
        println("Cancelling child")

        child.cancel()
        child.join()
        println("Parent is not cancelled")
    }
    job.join()
}

// If a coroutine encounters exception other than `CancellationException`, it
// cancels its parent with that exception. This behaviour cannot be overridden
// and is used to provide stable coroutines hierarchies for
// `structured concurrency` which do not depend on `CoroutineExceptionHandler`.
private fun cancellationAndExceptions_cancelWithCause() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    val job = GlobalScope.launch(handler) {
        // the first child
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                withContext(NonCancellable) {
                    println("Children are cancelled, but exception is not handled until all children terminate")
                    delay(100)
                    println("The first child finished its non cancellable block")
                }
            }
        }
        // the second child
        launch {
            delay(10)
            println("Second child throws an exception")
            throw ArithmeticException()
        }
    }
    job.join()
}

private fun exceptionsAggregation() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception with suppressed ${exception.suppressed.contentToString()}")
    }
    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                throw ArithmeticException()
            }
        }
        launch {
            delay(100)
            throw IOException()
        }
        delay(Long.MAX_VALUE)
    }
    job.join()
}
