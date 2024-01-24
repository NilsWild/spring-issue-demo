package de.example

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.UUID

@Node
class Person() {

    constructor(name: String, lastname: String) : this() {
        this.name = name
        this.lastname = lastname
    }

    @Id
    var id = UUID.randomUUID()

    lateinit var name: String
    lateinit var lastname: String

    @Relationship(type = "FRIEND_OF")
    var friends: Set<Person> = setOf()

    @Relationship(type = "RELATED_TO")
    var family: Set<Person> = setOf()

    @Version
    var version: Long = 0
}

interface PersonWithFriendsAndFamily {
    val id: UUID
    val name: String
    val lastname: String
    val family: Set<Family>
    val friends: Set<Friends>

    interface Friends {
        val id: UUID
        val name: String
        val friends: Set<FriendsOfFriends>

        interface FriendsOfFriends {
            val id: UUID
            val name: String
        }
    }

    interface Family{
        val id: UUID
        val lastname: String
    }
}