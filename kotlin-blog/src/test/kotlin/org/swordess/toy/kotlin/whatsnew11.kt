package org.swordess.toy.kotlin

import kotlin.NoSuchElementException

/****************/
/* Type aliases */
/****************/

typealias OscarWinners = Map<String, String>

fun countLaLaLand(oscarWinners: OscarWinners) =
        oscarWinners.count { it.value.contains("La La Land") }

// Note that the type names (initial and the type alias) are interchangeable
fun checkLaLaLandIsTheBestMovie(oscarWinners: Map<String, String>) =
        oscarWinners["Best Movie"] == "La La Land"

fun votesForLaLaLand(votes: List<Map<String, String>>) =
        votes.map(::countLaLaLand).sum()



/****************************/
/* Bound callable reference */
/****************************/

class Vehicle(val brand: String, val no: String) {
    // shorter syntax for properties
    val displayName: String get() = "$brand: $no"
}

fun decorateName(nameProvider: () -> String): String = "[ ${nameProvider.invoke()} ]"



/*************************/
/* Destructing in lambda */
/*************************/

data class Stub(val seq: Int, val desc: String)



/*****************************/
/* Inline property accessors */
/*****************************/

// You can now mark property accessors with the `inline` modifier if the
// property don't have a backing field. Such accessors are compiled in
// the way as `inline functions`
val <T> List<T>.lastIndex: Int
    inline get() = this.size - 1

class TryInlinePropertyAccessor(var kind: String) {

    // You can also mark the entire property as `inline` - then the modifier is applied to both accessors
    inline var some: String
        set(value) {
            kind = (if (value.startsWith("some ", ignoreCase = true)) value.substring(6) else value)
        }
        get() = "some $kind"
}



/******************************/
/* Local delegated properties */
/******************************/

fun demoLocalDelegatedProperties(needAnswer: () -> Boolean) {
    val answer: Int by lazy {
        println("Calculating the answer...")
        42
    }
    if (needAnswer()) {
        println("The answer is $answer")
        println("this invocation will not print the debug msg")
    } else {
        println("Sometimes no answer is the answer...")
    }
}



/* Generic enum value access */

enum class RGB { RED, GREEN, BLUE }

inline fun <reified T : Enum<T>> printAllValue() {
    // Kotlin 1.1 SDK's enumValues is a good example for this use case
    println(enumValues<T>().joinToString { it.name })
}


fun main(args: Array<String>) {
    println(votesForLaLaLand(listOf(
            mapOf(
                    "Jame" to "La La Land",
                    "Bruno" to "sth. else1",
                    "Mars" to "sth. else2"),
            mapOf(
                    "Penny" to "sth. else3",
                    "Amen" to "La La Land"),
            mapOf(
                    "Penny" to "sth. else4",
                    "Bruno" to "sth. else2")
    )))

    val vehicle = Vehicle(brand = "Peking Jeep", no = "123456")
    println("decorated name is: ${decorateName(vehicle::displayName)}")

    val map = mapOf<Int, String>(1 to "one", 2 to "two")
    map.forEach {
        println("k: ${it.key}, v: ${it.value}")
    }
    map.forEach { k, v ->
        println("k: $k, v: $v")
    }

    val stub = Stub(1, "first try")
    val (seq, desc) = stub
    println("seq: $seq, desc: $desc")


    /*************************************/
    /* Underscores for unused parameters */
    /*************************************/

    val (_, desc2) = stub
    println("desc2: $desc2")


    demoLocalDelegatedProperties { true }
    demoLocalDelegatedProperties { false }

    enumValues<RGB>().forEach(::println)
    println(enumValueOf<RGB>("BLUE"))
    printAllValue<RGB>()


    /********************/
    /* Standard library */
    /********************/

    // String.toIntOrNull, String.toLongOrNull, etc.
    println("123".toIntOrNull())

    // onEach
    val arr = arrayOf("China", "Germany", "England", "Poland", "Singapore")
    println(arr
            .filter { it.contains("e") }
            .onEach { println("El: $it") }
            .map { "prefix-$it" }
            .joinToString { it })

    println("Array.toString: $arr")
    println("Array.contentToString: ${arr.contentToString()}")

    arr.apply {
    }

    arr.also {
    }

    arr.groupingBy {
    }

    // Map.minus(key)
    val m = mapOf("key" to 42)
    val emptyMap = m - "key"
    println(emptyMap)

    val mapDelegate = mutableMapOf<String, Any>()
            .withDefault { if (it == "valueDelegated") "a" to "aaa" else throw NoSuchElementException(it) }
//            .withDefault { if (it == "anotherValueDelegated") "b" to "bbb" else throw NoSuchElementException(it) }
    var valueDelegated: Pair<String, String> by mapDelegate
    var anotherValueDelegated: Pair<String, String> by mapDelegate
    println(valueDelegated)
//    println(anotherValueDelegated)
}
