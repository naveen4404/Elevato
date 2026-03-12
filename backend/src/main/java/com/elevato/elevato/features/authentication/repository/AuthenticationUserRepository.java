package com.elevato.elevato.features.authentication.repository;

import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser,Long> {

    Optional<AuthenticationUser> findByEmail(String email);

    List<AuthenticationUser> findByIdNot(Long id);
}
