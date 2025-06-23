package com.secureauth.repository;

import com.secureauth.entity.Role;
import com.secureauth.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing Role entities.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by name.
     *
     * @param name the role name
     * @return an Optional containing the role, if found
     */
    Optional<Role> findByName(RoleType name);
}