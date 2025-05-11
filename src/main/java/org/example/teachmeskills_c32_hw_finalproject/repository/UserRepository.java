package org.example.teachmeskills_c32_hw_finalproject.repository;

import jakarta.validation.constraints.Email;
import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@Email String email);
}
