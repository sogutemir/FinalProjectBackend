package org.work.personnelinfo.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.work.personnelinfo.admin.model.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("SELECT r FROM RoleEntity r WHERE r.name = ?1")
    Optional<RoleEntity> findByName(String roleName);
}
