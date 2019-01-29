package org.swordess.somekotlin.blog

class Node<T>(val value: T, val next: () -> Node<T>)

class Stuff {

    lateinit var lateinitVar: Any

    /* checking whether a lateinit var is initialized */
    fun checkInitialized() {
        // NOTE: only available inside the enclosing class
        println(::lateinitVar.isInitialized)
        lateinitVar = "value"
        println(::lateinitVar.isInitialized)
    }

}

fun main(args: Array<String>) {
    /* ************************************************* */
    /* lateinit top-level properties and local variables */
    /* ************************************************* */

    lateinit var third: Node<Int>

    val second = Node(2, next = { third })
    val first = Node(1, next = { second })

    third = Node(3, next = { first })

    val nodes = generateSequence(first) { it.next() }
    println("Values in the cycle: ${nodes.take(7).joinToString { it.value.toString() }}, ...")

    val s = Stuff()
    s.checkInitialized()

    windowed_chunked_zipWithNext()
    fill_replaceAll_shuffle_shuffled()
}

private fun windowed_chunked_zipWithNext() {
    val items = (1..9).map { it * it }

    // [[1, 4, 9, 16], [25, 36, 49, 64], [81]]
    val chunkedIntoLists = items.chunked(4)

    // [(1, 4, 9), (16, 25, 36), (49, 64, 81)]
    val points3d = items.chunked(3) { (x, y, z) -> Triple(x, y, z) }

    // [[1, 4, 9, 16], [4, 9, 16, 25], [9, 16, 25, 36], [16, 25, 36, 49], [25, 36, 49, 64], [36, 49, 64, 81]]
    val windowed = items.windowed(4)

    // [7.5, 13.5, 21.5, 31.5, 43.5, 57.5]
    val slidingAverage = items.windowed(4) { it.average() }

    // [3, 5, 7, 9, 11, 13, 15, 17]
    val pairwiseDifferences = items.zipWithNext { a, b -> b - a }
}

private fun fill_replaceAll_shuffle_shuffled() {
    val items = (1..5).toMutableList()

    items.shuffle()
    println("Shuffled items: $items")

    items.replaceAll { it * 2 }
    println("Items doubled: $items")

    items.fill(5)
    println("Items filled with 5: $items")
}
