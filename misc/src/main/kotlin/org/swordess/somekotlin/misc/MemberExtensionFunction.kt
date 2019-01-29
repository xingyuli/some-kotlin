package org.swordess.somekotlin.misc

enum class HttpMethod {
    GET,
    POST
}

class Path(val uri: String, val method: HttpMethod)

class Handler(val basePath: String) {

    private val routes = mutableMapOf<Path, () -> Unit>()

    fun get(uri: String, block: () -> Unit) {
        routes[Path(basePath + uri, HttpMethod.GET)] = block
    }

    fun post(uri: String, block: () -> Unit) {
        routes[Path(basePath + uri, HttpMethod.POST)] = block
    }

}

class HandlerWithExtension(val basePath: String) {

    private val routes = mutableMapOf<Path, () -> Unit>()

    fun String.get(block: () -> Unit) {
        routes[Path(basePath + this, HttpMethod.GET)] = block
    }

    fun String.post(block: () -> Unit) {
        routes[Path(basePath + this, HttpMethod.GET)] = block
    }

}

fun main(args: Array<String>) {
    Handler("/users").apply {
        get("/{id}") {
            println("get user info for ...")
        }
        post("/{id}") {
            println("update user info for ...")
        }
    }

    HandlerWithExtension("/users").apply {
        "{/id}".get {
            println("get user info for ...")
        }
        "{/id}".post {
            println("update user info for ...")
        }
    }
}