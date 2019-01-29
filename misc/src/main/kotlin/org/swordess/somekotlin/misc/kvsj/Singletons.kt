package org.swordess.somekotlin.misc.kvsj

class CompanionSingleton private constructor() {

    companion object {

        val instance: CompanionSingleton by lazy {
            CompanionSingleton()
        }

    }

}

class StandardSingleton private constructor() {

    companion object {

        private val instance_: StandardSingleton by lazy {
            StandardSingleton()
        }

        @JvmStatic
        fun getInstance() = instance_

    }

}

object ShorthandSingleton {
    init {
        println("do some preparation here")
    }
}
