package org.example.teachmeskills_c32_hw_finalproject.repository;

import org.example.teachmeskills_c32_hw_finalproject.model.users.Security;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityRepository extends JpaRepository<Security, Long> {
    Boolean existsByLogin(String login);
}
