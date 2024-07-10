package de.example

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest
class SpringAppTest: Neo4jBaseTest() {

    @Autowired
    lateinit var repository: Repository

    @Autowired
    lateinit var neo4jTemplate: Neo4jTemplate

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun testGraphql() {
        var client: WebTestClient = WebTestClient.bindToApplicationContext(context)
            .configureClient()
            .baseUrl("/graphql")
            .build()

        var tester: HttpGraphQlTester = HttpGraphQlTester.create(client)

        val query = "{ firstEntities { secondEntities { labels prev { labels } } } }"
        val getFirstEntities: List<FirstEntity> = tester.document(query)
            .execute()
            .path("data.firstEntities[*]")
            .entityList(FirstEntity::class.java)
            .get()
        Assertions.assertTrue(getFirstEntities.isNotEmpty())
        Assertions.assertNotNull(getFirstEntities[0].secondEntities)
        Assertions.assertTrue(getFirstEntities[0].secondEntities.any{ it.prev.isNotEmpty() })
    }

}