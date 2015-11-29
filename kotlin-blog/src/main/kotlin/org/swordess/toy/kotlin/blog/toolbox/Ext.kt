package org.swordess.toy.kotlin.blog.toolbox

fun Statistics.plus(other: Statistics): Statistics =
        Statistics(this.failedNum + other.failedNum, this.succeededNum + other.succeededNum)
