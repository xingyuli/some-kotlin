package org.swordess.toy.kotlin.misc.socket

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket

fun main(args: Array<String>) {
    val serverSocket = ServerSocket()
    serverSocket.bind(InetSocketAddress(InetAddress.getLocalHost(), 9093))

    println("waiting for connections....")

    serverSocket.accept().use {
        val input = it.inputStream.bufferedReader()
        val output = it.outputStream.writer()

        output.write("Server got: ${input.readLine()}\n")
        output.write("Bye!\n")

        output.close()
        input.close()
    }

    serverSocket.close()
    println("server stopped.")
}
