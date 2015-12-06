package org.swordess.toy.kotlin.wasabi.interceptor

import org.wasabi.app.AppServer
import org.wasabi.interceptors.enableContentNegotiation
import org.wasabi.interceptors.serveStaticFilesFromFolder

fun main(args: Array<String>) {
    val appServer = AppServer()

    // enable ContentNegotiationInterceptor
    appServer.enableContentNegotiation()


    val userDir = System.getProperty("user.dir")
    println("user.dir: $userDir")

    // TODO Q: is it possible to use classpath relative path?
    val publicFolder = "$userDir/wasabi/src/main/resources/public"
    println("public folder: $publicFolder")

    // enable StaticFileInterceptor
    appServer.serveStaticFilesFromFolder(publicFolder)


    appServer.start()
}