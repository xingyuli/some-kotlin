package org.swordess.somekotlin.blog.ranges

import org.junit.Assert
import org.junit.Test

class RangeTest {

    @Test
    fun testIn() {
        // ".." equivalents to "rangeTo"
        // "a in b" equivalents to "b.contains(a)"
        // "a !in b" equivalents to "!b.contains(a)"
        Assert.assertTrue("isolated" in ("island".."isz"))
    }

    @Test
    fun testReverseIteration() {
        // for reverse iteration, downTo must be used

        // nothing will be iterated
        var r = ""
        for (x in 4..1) {
            r += x
        }
        Assert.assertEquals("", r)

        // ok, works as expected
        r = ""
        for (x in 4 downTo 1) {
            r += x
        }
        Assert.assertEquals("4321", r)
    }

}