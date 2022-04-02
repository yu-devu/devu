package com.devu.backend.repository;

import com.devu.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmailAuthKey(String authKey);

    Optional<User> findByEmail(String email);
}
