package org.swordess.somekotlin.elasticsearch

data class ClientConfig(val host: String, val port: Int, val clusterName: String, val indexName: String)