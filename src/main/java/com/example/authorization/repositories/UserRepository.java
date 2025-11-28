package com.example.authorization.repositories;


import com.example.authorization.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.authorities"})
    Optional<User> findDetailedByUsername(String username); // renamed method
}