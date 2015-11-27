package org.swordess.toy.kotlin.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import java.net.InetAddress

class IndexRepository(val config: ClientConfig) {

    private lateinit var client: Client

    init {
        println("using config: $config")

        val settings = Settings.settingsBuilder().put("cluster.name", config.clusterName).build()
        client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(InetSocketTransportAddress(InetAddress.getByName(config.host), config.port))

        val indicesExistsResponse = client.admin().indices().prepareExists(config.indexName).execute().actionGet()
        if (!indicesExistsResponse.isExists) {
            client.admin().indices().prepareCreate(config.indexName).execute().actionGet()
        }

        client.admin().indices().preparePutMapping(config.indexName)
                .setType("CompanyDocument")
                .setSource(MAPPING_SETTING)
                .execute().actionGet()

        println("mapping setting updated")
    }

    companion object {

        val MAPPING_SETTING: XContentBuilder = XContentFactory.jsonBuilder().prettyPrint()
                .startObject()
                    .startObject("CompanyDocument")
                        .startObject("properties")
                            .startObject("level")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()

    }

}
