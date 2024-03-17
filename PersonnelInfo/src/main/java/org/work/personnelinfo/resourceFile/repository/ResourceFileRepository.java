package org.work.personnelinfo.resourceFile.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.resourceFile.model.ResourceFileEntity;

import java.util.Optional;

public interface ResourceFileRepository extends JpaRepository<ResourceFileEntity, Long> {
    @Query("SELECT f FROM ResourceFileEntity f WHERE f.name = :name")
    Optional<ResourceFileEntity> findByName(@Param("name") String name);

    @Query("SELECT f FROM ResourceFileEntity f WHERE f.id = :id")
    Optional<ResourceFileEntity> findById(@Param("id") Long id);




}
