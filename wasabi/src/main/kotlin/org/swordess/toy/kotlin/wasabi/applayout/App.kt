package org.swordess.toy.kotlin.wasabi.applayout

import org.wasabi.app.AppServer

fun main(args: Array<String>) {
    val appServer = AppServer()
    appServer.get("/customer", getCustomers)
    appServer.get("/customer/:id", getCustomerById)
    appServer.start()
}