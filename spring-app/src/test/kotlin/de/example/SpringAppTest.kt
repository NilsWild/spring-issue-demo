package de.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    fun `save and retrieve`() {
        val bs = setOf(B(), B())
        val a = A(bs)
        neo4jTemplate.saveAs(a, AProjection::class.java)
        val retrieved = repository.findProjectionById(a.id)!!
        assertEquals(2, retrieved.bs.size)

        val newBs = setOf(B())
        a.bs = newBs
        neo4jTemplate.saveAs(a, AProjection::class.java)

        val retrievedNew = repository.findProjectionById(a.id)!!
        assertEquals(1,retrievedNew.bs.size)

    }


    @Test
    fun `save and retrieve2`() {
        val bs = setOf(B(), B())
        val a = A(bs)
        neo4jTemplate.saveAs(a, AProjection::class.java)
        val retrieved = repository.findProjectionById(a.id)!!
        assertEquals(2, retrieved.bs.size)

        a.bs = emptySet()
        neo4jTemplate.saveAs(a, AProjection::class.java)

        val retrievedNew = repository.findProjectionById(a.id)!!
        assertEquals(0, retrievedNew.bs.size)

    }

}