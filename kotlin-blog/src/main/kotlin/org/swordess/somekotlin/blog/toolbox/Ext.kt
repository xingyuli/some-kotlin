package org.swordess.somekotlin.blog.toolbox

operator fun Statistics.plus(other: Statistics): Statistics =
    Statistics(
        this.failedNum + other.failedNum,
        this.succeededNum + other.succeededNum
    )


data class User(val firstName: String, val lastName: String)

fun <R> withUser(user: User, f: User.() -> R): R {
    println("User login: ${user.firstName} ${user.lastName}")
    val r = user.f()
    println("User logout: ${user.firstName} ${user.lastName}")
    return r
}