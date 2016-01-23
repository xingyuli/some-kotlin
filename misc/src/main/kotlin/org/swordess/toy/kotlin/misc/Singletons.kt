package org.swordess.toy.kotlin.misc

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

        @JvmStatic fun getInstance() = instance_

    }

}
