package org.swordess.toy.kotlin

fun main(args: Array<String>) {
    /* ***** Contracts ***** */
    foo("hello")
    bar()
    baz("abc")


    /* ***** Standard library ***** */

    // ifEmpty and ifBlank functions
    println("".ifEmpty { "nothing" })
    println(" ".ifBlank { "nothing again" })

    // Sealed classes in reflection
    println(Shape::class.sealedSubclasses)
}

fun foo(s: String?) {
    require(s is String)
    println(s.length)
}

object MyLock

fun bar() {
    val x: Int
    synchronized(MyLock) {
        x = 42
    }
    println(x)
}

fun baz(x: String?) {
    if (!x.isNullOrEmpty()) {
        println("length of '$x' is ${x.length}")
    }
}

sealed class Shape(val name: String) {
    class Circle(name: String) : Shape(name)
    class Triangle(name: String) : Shape(name)
    class Square(name: String) : Shape(name)
}
