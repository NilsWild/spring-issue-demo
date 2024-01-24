package de.example

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
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var neo4jTemplate: Neo4jTemplate

    @Test
    fun `save and retrieve friends`() {
        val person = Person("Alice", "Smith")
        val friend = Person("Bob", "Kelso")

        person.friends = setOf(friend)
        person.family = setOf(friend)
        friend.friends = setOf(person)

        personRepository.save(person)

        val retrievedPerson = personRepository.findPersonWithFriendsAndFamilyById(person.id)

        println(retrievedPerson!!.friends.first().name)
        println(retrievedPerson!!.family.first().lastname)
        println(retrievedPerson!!.friends.first().friends.first().name)

    }
}