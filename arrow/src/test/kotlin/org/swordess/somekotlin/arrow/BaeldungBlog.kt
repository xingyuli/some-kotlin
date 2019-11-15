package org.swordess.somekotlin.arrow

import arrow.core.*
import org.junit.Assert
import org.junit.Test

class MonadsTest {

    @Test
    fun testId() {
        val id = Id("foo")
        val justId = Id.just("foo")

        Assert.assertEquals("foo", id.extract())
        Assert.assertEquals(justId, id)
    }

    @Test
    fun testOption() {
        val factory = Option.just(42)
        val constructor = Option(42)
        val emptyOptional = Option.empty<Int>()
        val fromNullable = Option.fromNullable(null)

        Assert.assertEquals(42, factory.getOrElse { -1 })
        Assert.assertEquals(factory, constructor)
        Assert.assertEquals(emptyOptional, fromNullable)
    }

    @Test
    fun testEither() {
        val rightOnly: Either<String, Int> = Either.right(42)
        val leftOnly: Either<String, Int> = Either.left("foo")

        Assert.assertTrue(rightOnly.isRight())
        Assert.assertTrue(leftOnly.isLeft())
        Assert.assertEquals(42, rightOnly.getOrElse { -1 })
        Assert.assertEquals(-1, leftOnly.getOrElse { -1 })

        Assert.assertEquals(0, rightOnly.map { it % 2 }.getOrElse { -1 })
        Assert.assertEquals(-1, leftOnly.map { it % 2 }.getOrElse { -1 })
        Assert.assertTrue(rightOnly.flatMap { Either.Right(it % 2) }.isRight())
        Assert.assertTrue(leftOnly.flatMap { Either.Right(it % 2) }.isLeft())
    }

    @Test
    fun testEvalWithNow() {
        val now = Eval.now(1)
        var counter: Int = 0
        // The map and flatMap operations will be executed lazily
        val map = now.map { x ->
            counter++
            x + 1
        }
        Assert.assertEquals(0, counter)

        val extract = map.value()
        Assert.assertEquals(2, extract)
        Assert.assertEquals(1, counter)
    }

    @Test
    fun testEvalWithLater() {
        var counter: Int = 0
        val later = Eval.later {
            counter++
            counter
        }
        Assert.assertEquals(0, counter)

        val firstValue = later.value()
        Assert.assertEquals(1, firstValue)
        Assert.assertEquals(1, counter)

        val secondValue = later.value()
        Assert.assertEquals(1, secondValue)
        Assert.assertEquals(1, counter)
    }

    @Test
    fun testEvalWithAlways() {
        var counter: Int = 0
        val later = Eval.always {
            counter++
            counter
        }

        val firstValue = later.value()
        Assert.assertEquals(1, firstValue)
        Assert.assertEquals(1, counter)

        val secondValue = later.value()
        Assert.assertEquals(2, secondValue)
        Assert.assertEquals(2, counter)
    }

    @Test
    fun errorHandlingWithOption() {
        println(computeWithOptionClient("abc"))
    }

    private fun computeWithOptionClient(input: String): String {
        return when (val computeOption = computeWithOption(input)) {
            is Some -> "The greatest divisor is square number: ${computeOption.t}"
            is None -> "Not an even number!"
        }
    }

    private fun computeWithOption(input: String): Option<Boolean> {
        return parseInput(input)
            .filter(::isEven)
            .map(::biggestDivisor)
            .map(::isSquareNumber)
    }

    private fun parseInput(s: String): Option<Int> = Option.fromNullable(s.toIntOrNull())

    private fun isEven(x: Int): Boolean = x % 2 == 0

    private fun biggestDivisor(x: Int): Int = x

    private fun isSquareNumber(x: Int): Boolean = Math.pow(Math.floor(Math.sqrt(x.toDouble())), 2.0).toInt() == x

    @Test
    fun errorHandlingWithEither() {
        println(computeWithErrorClient("abc"))
    }

    private fun computeWithErrorClient(input: String): String {
        return when (val computeWithError = computeWithError(input)) {
            is Either.Right -> "The greatest divisor is square number: ${computeWithError.b}"
            is Either.Left -> when (computeWithError.a) {
                is ComputeProblem.NotANumber -> "Wrong input! Not a number!"
                is ComputeProblem.OddNumber -> "It is an odd number!"
            }
        }
    }

    private fun computeWithError(input: String): Either<ComputeProblem, Boolean> {
        return parseInputV2(input)
            .filterOrElse(::isEven) { ComputeProblem.OddNumber }
            .map(::biggestDivisor)
            .map(::isSquareNumber)
    }

    sealed class ComputeProblem {
        object OddNumber : ComputeProblem()
        object NotANumber : ComputeProblem()
    }

    private fun parseInputV2(s: String): Either<ComputeProblem, Int> =
        Either.cond(s.toIntOrNull() != null, { s.toInt() }, { ComputeProblem.NotANumber })

}
