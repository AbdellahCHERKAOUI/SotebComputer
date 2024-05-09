package com.example.computershop.Repositories;

import com.example.computershop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByFirstName(String username);

    Boolean existsByFirstName(String username);

    Boolean existsByEmail(String email);
}
