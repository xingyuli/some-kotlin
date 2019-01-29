package org.swordess.somekotlin.misc.kvsj

import org.junit.Test
import org.swordess.somekotlin.misc.kvsj.JavaBean

class JavaBeanAccessorTest {

    private val bean = JavaBean()

    @Test
    fun test() {
        // field 'name' of String, with getName and setName
        bean.name = "set name"
        println(bean.name)

        // field 'hasName' of boolean, with isHasName and setHasName
        bean.isHasName = true
        println(bean.isHasName)

        // field 'hasAge' of boolean, with getHasAge and setHasAge
        bean.hasAge = false
        println(bean.hasAge)

        // field 'isValid' of boolean, with isIsValid and setIsValid
        bean.isIsValid = true
        println(bean.isIsValid)

        // field 'active' of boolean, with getActive and setActive
        bean.active = true
        println(bean.active)

        // field 'used' of boolean, with isUsed and setUsed
        bean.isUsed = true
        println(bean.isUsed)

        println(bean.age)
        println(bean.hasPressure)
        println(bean.isOld)
        println(bean.isFantasy)
    }

}