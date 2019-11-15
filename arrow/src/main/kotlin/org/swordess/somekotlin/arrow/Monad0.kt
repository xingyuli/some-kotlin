package org.swordess.somekotlin.arrow

fun main() {
//    example1()
//    example2()
//    example3()
    example4()
}

private fun example1() {
    val sine = { x: Double -> Math.sin(x) }
    val cube = { x: Double -> x * x * x }

    println(sine(0.123))
    println(cube(0.987))

    val sineCubed = { x: Double -> sine(cube(x)) }
    println(sineCubed(1.22))

    val sineOfCube = compose(sine, cube)
    println(sineOfCube(1.22))
    println(compose(sine, cube)(1.22))
}

private fun example2() {
    val sine = { x: Double -> Math.sin(x) to "sine was called." }
    val cube = { x: Double -> x * x * x to "cube was called." }

    println(sine(0.123))
    println(cube(0.987))

    println(composeDebuggable(sine, cube)(1.22))
}

fun <P, Q, R> composeDebuggable(f: (Q) -> Pair<R, String>, g: (P) -> Pair<Q, String>) = { x: P ->
    val (q, qs) = g(x)
    val (r, rs) = f(q)
    r to (qs + rs)
}

private fun example3() {
    val sine = { x: Double -> Math.sin(x) to "sine was called." }
    val cube = { x: Double -> x * x * x to "cube was called." }

    val f = compose(bind(sine), bind(cube))
    println(f(unit(3.0)))
}

private fun example4() {
    val sine = { x: Double -> Math.sin(x) to "sine was called." }
    val round = { x: Double -> Math.round(x).toDouble() }

    // lift(round) convert
    //   Double -> Long
    // to
    //   Double -> (Long, String)
    val roundDebug: (Double) -> Pair<Double, String> = lift(round)

    // bind(roundDebug) convert
    //   Double           -> (Long, String)
    // to
    //   (Double, String) -> (Long, String)
    val f: (Pair<Double, String>) -> Pair<Double, String> = bind(roundDebug)

    // bind(sine) convert
    //   Double           -> (Double, String)
    // to
    //   (Double, String) -> (Double, String)
    val g: (Pair<Double, String>) -> Pair<Double, String> = bind(sine)

    // so g:
    //   (Double, String) -> (Double, String)
    // f:
    //   (Double, String) -> (Long, String)
    // eventually:
    //   (Double, String) -> (Long, String)
    val eventually = compose(f, g)

    println(eventually(unit(27.0)))
}

// example4 的 inline 版本
private fun example5() {
    val sine = { x: Double -> Math.sin(x) to "sine was called." }
    val round = { x: Double -> Math.round(x).toDouble() }

    // lift then bind
    val eventually = compose(bind(lift(round)), bind(sine))
    println(eventually(unit(27.0)))
}

fun <P, Q, R> compose(f: (Q) -> R, g: (P) -> Q): (P) -> R = { x: P -> f(g(x)) }

/**
 * 适配 in types
 */
fun <T> bind(f: (T) -> Pair<T, String>): (Pair<T, String>) -> Pair<T, String> = { p: Pair<T, String> ->
    val (x, s) = p
    val (y, t) = f(x)
    y to (s + t)
}

fun <T> unit(x: T): Pair<T, String> = x to ""

/**
 * 适配 out types
 */
fun <U, V> lift(f: (U) -> V): (U) -> Pair<V, String> {
    // f: (U) -> V
    // unit: (V) -> Pair<V, String>
    // ----> compose(unit, f) returns (U) -> Pair<V, String>
    return compose(::unit, f)
}
