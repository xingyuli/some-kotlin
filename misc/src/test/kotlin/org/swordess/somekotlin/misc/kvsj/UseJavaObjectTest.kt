package org.swordess.somekotlin.misc.kvsj

import org.junit.Test
import org.swordess.somekotlin.misc.kvsj.UseJavaObject

class UseJavaObjectTest {

    @Test
    fun test() {
        // Object -> Any?
        UseJavaObject.doSomething(Any())
    }

}