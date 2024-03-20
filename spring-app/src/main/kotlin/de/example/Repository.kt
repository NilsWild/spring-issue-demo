package de.example

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface Repository: org.springframework.data.repository.CrudRepository<A, UUID> {
    fun findProjectionById(id: UUID): AProjection?
}