package org.swordess.toy.kotlin.blog.delegate

import java.time.Instant
import java.util.Date
import kotlin.reflect.KProperty

class Account {

    var validThrough: Date? by DateDelegate()

    class DateDelegate {

        var _value: Date? = null

        fun getValue(thisRef: Account, prop: KProperty<*>): Date? {
            println("get has been called")
            return _value
        }

        fun setValue(thisRef: Account, prop: KProperty<*>, value: Date?) {
            println("set has been called")
            _value = value;
        }

    }

}

fun main(args: Array<String>) {
    val account = Account()
    println(account.validThrough)

    account.validThrough = Date.from(Instant.now())
    println(account.validThrough)
}