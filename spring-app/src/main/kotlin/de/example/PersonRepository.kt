package de.example

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PersonRepository: org.springframework.data.repository.CrudRepository<Person, UUID> {
    fun findPersonWithFriendsAndFamilyById(id: UUID): PersonWithFriendsAndFamily?
}