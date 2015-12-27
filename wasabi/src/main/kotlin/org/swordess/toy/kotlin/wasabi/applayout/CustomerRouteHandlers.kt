package org.swordess.toy.kotlin.wasabi.applayout

import org.swordess.toy.kotlin.wasabi.Customer
import org.wasabi.routing.RouteHandler
import org.wasabi.routing.routeHandler

private val customers = listOf(
        Customer("Vic", "vic@demo.com"),
        Customer("Henry", "henry@demo.com"),
        Customer("Daisy", "daisy@demo.com")
)

val getCustomers = routeHandler {
//    response.send(customers)
}

/*
val getCustomerById = routeHandler {
    request.routeParams["id"]!!.let { id ->
        response.send(customers[id.toInt()])
    }
}
*/

// routeHandler is a syntatic sugar to define the type of the route handler.
// You could also have write that as:
val getCustomerById: RouteHandler.() -> Unit = {
    request.routeParams["id"]!!.let { id ->
        response.send(customers[id.toInt()])
    }
}