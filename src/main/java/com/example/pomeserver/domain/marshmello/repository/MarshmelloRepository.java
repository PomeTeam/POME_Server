package com.example.pomeserver.domain.marshmello.repository;

import com.example.pomeserver.domain.marshmello.entity.Marshmello;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarshmelloRepository extends JpaRepository<Marshmello, Long> {
    Optional<Marshmello> findByUser(User user);
}
