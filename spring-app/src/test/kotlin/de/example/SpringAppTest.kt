package de.example

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class SpringAppTest: Neo4jBaseTest() {

    @Autowired
    lateinit var repository: Repository

    @Autowired
    lateinit var neo4jTemplate: Neo4jTemplate

    @Test
    fun `save and retrieve map b2 first`() {
        val b1 = B(setOf("label"), setOf())
        val b2 = B(setOf("label"), setOf(B(setOf(), setOf()).apply {
            id = b1.id
            version = b1.version
        }))
        val a = A(setOf(b2, b1))
        val aResult = neo4jTemplate.saveAs(a, AProjection::class.java)

        val queried = repository.findProjectionById(aResult.id)!!

        assertEquals(queried.bs.first().labels,setOf("label"))
        assertEquals(queried.bs.last().labels,setOf("label"))
    }

    @Test
    fun `save and retrieve map b1 first`() {
        val b1 = B(setOf("label"), setOf())
        val b2 = B(setOf("label"), setOf(B(setOf(), setOf()).apply {
            id = b1.id
            version = b1.version
        }))
        val a = A(setOf(b1, b2))
        val aResult = neo4jTemplate.saveAs(a, AProjection::class.java)

        val queried = repository.findProjectionById(aResult.id)!!

        assertEquals(queried.bs.first().labels,setOf("label"))
        assertEquals(queried.bs.last().labels,setOf("label"))
    }

}