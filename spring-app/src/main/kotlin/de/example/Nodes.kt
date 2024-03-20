package de.example

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
}

@Node
class B() {
    @Id
    var id: UUID = UUID.randomUUID()
}

interface AProjection {
    val id: UUID
    val bs: Set<BProjection>

    interface BProjection {
        val id: UUID
    }
}

