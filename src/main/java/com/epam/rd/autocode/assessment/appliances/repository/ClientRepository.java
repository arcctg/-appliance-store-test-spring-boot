package com.epam.rd.autocode.assessment.appliances.repository;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Client} entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Finds a client by their email.
     *
     * @param email the email of the client
     * @return an {@link Optional} containing the client if found, or empty if not
     */
    Optional<Client> findByEmail(String email);

    /**
     * Checks if a client exists by their email.
     *
     * @param email the email of the client
     * @return true if a client with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
