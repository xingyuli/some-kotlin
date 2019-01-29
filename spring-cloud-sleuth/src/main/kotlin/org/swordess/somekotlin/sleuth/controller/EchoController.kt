package org.swordess.somekotlin.sleuth.controller

import brave.Tracer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("echo")
class EchoController {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var tracer: Tracer

    @GetMapping
    fun echo(@RequestParam content: String): String {
        val span = tracer.startScopedSpan("GET_dictionaries")
        try {
            val dictionaries =
                restTemplate.getForObject("http://localhost:9090/dcms/rest/dictionaries.json", Map::class.java)
            // snippet
        } catch (e: Exception) {
            span.error(e)
            throw e
        } finally {
            span.finish()
        }

        return "Echo $content"
    }

}
