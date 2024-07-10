package de.example

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.graphql.data.GraphQlRepository
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import java.util.UUID

@GraphQlRepository
interface Repository: Neo4jRepository<FirstEntity, UUID> {
}

@Component
class Service(private val repository: Repository): ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val secondEntity1 = SecondEntity(setOf("b1"), setOf())
        val secondEntity2 = SecondEntity(setOf("b2"), setOf(secondEntity1))
        val firstEntity = FirstEntity(setOf(secondEntity1, secondEntity2))
        val saved = repository.save(firstEntity)
    }
}

@Controller
class Controller(private val repository: Repository) {
    //@QueryMapping
    fun getFirstEntities(): List<FirstEntity> {
        return repository.findAll().toList()
    }
}