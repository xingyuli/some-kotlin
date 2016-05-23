package org.swordess.toy.kotlin.misc

fun main(args: Array<String>) {
    val literal = """
    |Line 1
    |Line 2
    |Line 3
    """.trimMargin()
    println("text is: $literal")

    val firstName = "Vic"
    val lastName = "Lau"
    val literalWithExpression = """
Authored by $firstName $lastName
"""
    println(literalWithExpression)
}