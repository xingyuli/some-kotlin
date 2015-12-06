package org.swordess.toy.kotlin.wasabi

import org.wasabi.app.AppServer

fun main(args: Array<String>) {
    var server = AppServer()
    server.get("/", { response.send("Hello World!") })
    server.start()
}