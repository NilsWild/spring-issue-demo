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
    lateinit var behaviorRepository: BehaviorRepository

    @Autowired
    lateinit var neo4jTemplate: Neo4jTemplate

    @Test
    fun `save and retrieve`() {
        val stimulus = ReceivedMessage.Stimulus("stimulus", "stimulusheaders")
        val componentReaction = ComponentReaction("componentReaction", "componentReactionHeaders", setOf(stimulus))
        val environmentReaction = ReceivedMessage.EnvironmentReaction("environmentReaction", "enviromentReactionHeaders", componentReaction)
        val behavior = Behavior(setOf(stimulus, componentReaction, environmentReaction))

        // val savedBehavior = behaviorRepository.save(behavior)
        val savedBehavior = neo4jTemplate.saveAs(behavior, BehaviorProjection::class.java)

        val queriedBehavior = behaviorRepository.findProjectionById(behavior.id)

        queriedBehavior?.let {
            //works
            assertNotNull(it.messages.first { it.payload == "environmentReaction" }.reactionTo)
            assertEquals(savedBehavior.messages.first { it.payload == "componentReaction" }.dependsOn!!.size, 1)

            //fails
            assertEquals(it.messages.first{ it.payload == "componentReaction" }.headers, "componentReactionHeaders")
            assertEquals(it.messages.first { it.payload == "componentReaction" }.dependsOn!!.size, 1)
        }

    }

    @Test
    fun `save and retrieve2`() {
        val stimulus = ReceivedMessage.Stimulus("stimulus", "stimulusheaders")
        val componentReaction = ComponentReaction("componentReaction", "componentReactionHeaders", setOf(stimulus))
        val behavior = Behavior(setOf(stimulus, componentReaction))

        // val savedBehavior = behaviorRepository.save(behavior)
        val savedBehavior = neo4jTemplate.saveAs(behavior, BehaviorProjection::class.java)

        val queriedBehavior = behaviorRepository.findProjectionById(behavior.id)

        queriedBehavior?.let {
            //works
            assertEquals(savedBehavior.messages.first { it.payload == "componentReaction" }.dependsOn!!.size, 1)

            //works
            assertEquals(it.messages.first{ it.payload == "componentReaction" }.headers, "componentReactionHeaders")
            assertEquals(it.messages.first { it.payload == "componentReaction" }.dependsOn!!.size, 1)
        }

    }
}