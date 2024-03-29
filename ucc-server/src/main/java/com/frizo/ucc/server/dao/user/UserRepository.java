package com.frizo.ucc.server.dao.user;

import com.frizo.ucc.server.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Page<User> findAllByEmailOrNameContains(String keywords1, String keywords2, Pageable pageable);

    List<User> findAllByEmailOrNameContains(String keywords1, String keywords2);

    List<User> findAllByEmail(String email);
}
