package de.example

import org.neo4j.cypherdsl.core.renderer.Dialect
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class SpringApp

fun main(args: Array<String>) {
    runApplication<SpringApp>(*args)
}

@Configuration
class Config {
    @Bean
    fun cypherDslConfiguration(): org.neo4j.cypherdsl.core.renderer.Configuration {
        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
            .withDialect(Dialect.NEO4J_5).build()
    }
}