package de.example

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BehaviorRepository: org.springframework.data.repository.CrudRepository<Behavior, UUID> {
    fun findProjectionById(id: UUID): BehaviorProjection?
}