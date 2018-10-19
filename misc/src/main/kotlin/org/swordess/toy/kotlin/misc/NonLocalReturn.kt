package org.swordess.toy.kotlin.misc

fun foo(block: () -> Unit) {
    block()
}

inline fun bar(block: () -> Int) = block()

inline fun baz(block: () -> Unit) {
    block()
}

fun main(args: Array<String>) {
    // non-local return: return statement in lambda

//     foo {
//        return // compilation error: return is not allowed here
//     }

    // by default non-local returns the out function, thus we need to use qualified return
    val result = bar {
        return@bar 3
    }
    println("result is 3: ${result == 3}")

    baz {
        return // inline function allows non-local return
    }

    println("won't reach")
}