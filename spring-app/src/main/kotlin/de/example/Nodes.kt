package de.example

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.DynamicLabels
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*


@Node
class FirstEntity() {
    constructor(secondEntities: Set<SecondEntity>): this(){
        this.id = UUID.randomUUID()
        this.secondEntities = secondEntities
    }
    @Id
    var id: UUID? = null

    @Relationship
    var secondEntities: Set<SecondEntity> = emptySet()

    @Version
    var version: Long? = null
}

@Node
class SecondEntity() {

    constructor(labels: Set<String>, prev: Set<SecondEntity>): this(){
        this.id = UUID.randomUUID()
        this.prev = prev
        this.labels = labels
    }

    @Id
    var id: UUID? = null

    @DynamicLabels
    var labels: Set<String> = emptySet()

    @Relationship
    var prev: Set<SecondEntity> = setOf()

    @Version
    var version: Long? = null
}
