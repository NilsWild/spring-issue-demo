package de.example

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.DynamicLabels
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*


@Node
class A() {
    constructor(bs: Set<B>): this(){
        this.bs = bs
    }
    @Id
    var id: UUID = UUID.randomUUID()
    @Relationship
    var bs: Set<B> = setOf()

    @Version
    var version: Long? = null
}

@Node
class B() {

    constructor(labels: Set<String>, prev: Set<B>): this(){
        this.prev = prev
        this.labels = labels
    }

    @Id
    var id: UUID = UUID.randomUUID()

    @DynamicLabels
    var labels: Set<String> = emptySet()

    @Relationship
    var prev: Set<B> = setOf()

    @Version
    var version: Long? = null
}

interface AProjection: EntityReferenceProjection {
    val bs: Set<BProjection>

    interface BProjection: EntityReferenceProjection {
        val prev: Set<EntityReferenceProjection>
        val labels: Set<String>
    }

}

interface EntityReferenceProjection {
    val id: UUID
    val version: Long?
}

