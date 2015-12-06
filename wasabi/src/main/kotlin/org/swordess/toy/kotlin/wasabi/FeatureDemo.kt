package org.swordess.toy.kotlin.wasabi

import org.slf4j.LoggerFactory
import org.wasabi.app.AppServer

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("ChainHandlerDemo")
    var server = AppServer()

    // chained handlers
    server.get("/",
            {
                log.info("URI requested is ${request.uri}")
                next()
            },
            {
                response.send("Hello World!")
            }
    )

    // route parameters
    server.get("/customer/:id", {
        val customerId = request.routeParams["id"]
        response.send("GET - Customer(id=$customerId): ...")
    })

    // query parameters
    server.get("/customer", {
        val customerName = request.queryParams["name"]
        response.send("GET - Customer(name=$customerName): ...")
    })

    // form parameters
    server.post("/customer", {
        val customerName = request.bodyParams["name"]
        response.send("POST - Customer(name=$customerName): ...")
    })

    server.start()
}

