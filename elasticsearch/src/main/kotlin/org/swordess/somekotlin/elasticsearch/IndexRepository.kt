package org.swordess.somekotlin.elasticsearch

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.search.SearchHit
import java.net.InetAddress

class IndexRepository(val config: ClientConfig) {

    private var client: Client

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

    fun forEachExisted(docType: String, vararg fields: String, handler: (SearchHit) -> Unit) {
        val searchRequestBuilder = client.prepareSearch(config.indexName).setTypes(docType).setFetchSource(false)
        if (fields.isEmpty()) {
            searchRequestBuilder.setNoFields()
        } else {
            fields.forEach { searchRequestBuilder.addField(it) }
        }

        val response = searchRequestBuilder.execute().actionGet()
        for (hit in response.hits) {
            handler(hit)
        }
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

fun main(args: Array<String>) {
    // demoForEachExisted()
}

private fun demoForEachExisted() {
    val clusterName = "yourClusterName"
    val indexName = "yourIndexName"

    val repository = IndexRepository(
        ClientConfig(
            "localhost",
            9300,
            clusterName,
            indexName
        )
    )
    repository.forEachExisted("FeeDocument", "feeId", "feeType") {
        val feeIdValue = it.field("feeId").value<String>()
        val feeTypeValue = it.field("feeType").value<String>()
        println("FeeDocument(id=${it.id},feeId=$feeIdValue,feeType=$feeTypeValue)")
    }
}
