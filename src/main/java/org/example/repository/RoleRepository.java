package org.example.repository;

import org.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findByRoleIn(Set<Role.ROLES> roles);
}
