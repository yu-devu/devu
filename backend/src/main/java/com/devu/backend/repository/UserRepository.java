package com.devu.backend.repository;

import com.devu.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);

    User findByEmailAuthKey(String authKey);

    User findByEmail(String email);
}
