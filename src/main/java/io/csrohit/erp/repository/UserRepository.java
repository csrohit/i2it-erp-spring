package io.csrohit.erp.repository;

import io.csrohit.erp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByUsername(String username);

    Page<User> findByNameContainingIgnoreCase(String searchParam, Pageable pageable);
}
