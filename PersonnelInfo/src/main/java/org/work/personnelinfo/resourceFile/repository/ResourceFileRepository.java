package org.work.personnelinfo.resourceFile.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.work.personnelinfo.resourceFile.model.ResourceFileEntity;

import java.util.Optional;

public interface ResourceFileRepository extends JpaRepository<ResourceFileEntity, Long> {
    @Query("SELECT f FROM ResourceFileEntity f WHERE f.name = ?1")
    Optional<ResourceFileEntity> findByName(String name);

    @Query("SELECT f FROM ResourceFileEntity f WHERE f.id = ?1")
    Optional<ResourceFileEntity> findById(Long id);


}
