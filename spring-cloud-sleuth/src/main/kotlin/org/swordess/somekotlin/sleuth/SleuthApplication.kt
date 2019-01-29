package org.swordess.somekotlin.sleuth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@SpringBootApplication
open class SleuthApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SleuthApplication::class.java, *args)
        }

    }

    @Configuration
    open class MyConfig {
        
        @Bean
        open fun restTemplate(): RestTemplate = RestTemplateBuilder().build()
        
    }

}