package de.example

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*


@Node
class Behavior() {
    constructor(messages: Set<Message>): this(){
        this.messages = messages
    }
    @Id
    var id: UUID = UUID.randomUUID()
    @Relationship(type = "OBSERVED")
    var messages: Set<Message> = setOf()
}

@Node
sealed class Message() {
    @Id
    var id: UUID = UUID.randomUUID()
   lateinit var payload: String
   lateinit var headers : String
}

@Node
abstract class ReceivedMessage() : Message() {
    @Node
    class Stimulus(): ReceivedMessage() {
        constructor(payload: String, headers: String): this(){
            this.payload = payload
            this.headers = headers
        }
    }

    @Node
    class EnvironmentReaction(): ReceivedMessage() {
        constructor(payload: String, headers: String, reactionTo: ComponentReaction): this(){
            this.payload = payload
            this.reactionTo = reactionTo
            this.headers = headers
        }

        @Relationship(type = "REACTION_TO")
        lateinit var reactionTo: ComponentReaction
    }
}

@Node
abstract class SentMessage() : Message() {
    @Relationship(type = "DEPENDS_ON")
    var dependsOn: Set<ReceivedMessage> = emptySet()
}

@Node
class ComponentReaction(): SentMessage() {
    constructor(payload: String, headers: String, dependsOn: Set<ReceivedMessage>): this(){
        this.payload = payload
        this.dependsOn = dependsOn
        this.headers = headers
    }
}

interface BehaviorProjection {

    val messages: Set<MessageProjection>

    interface MessageProjection {

        val payload: String
        val headers: String
        val dependsOn: Set<Dependency>?
        val reactionTo: Dependency?

        interface Dependency {
            val payload: String
        }
    }
}

