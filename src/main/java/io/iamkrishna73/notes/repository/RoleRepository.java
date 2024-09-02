package io.iamkrishna73.notes.repository;

import io.iamkrishna73.notes.entity.AppRole;
import io.iamkrishna73.notes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
     Optional<Role> findByRoleName(AppRole appRole);
}
