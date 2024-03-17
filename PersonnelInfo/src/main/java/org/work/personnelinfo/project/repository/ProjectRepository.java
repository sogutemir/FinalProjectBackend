package org.work.personnelinfo.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.project.model.ProjectEntity;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity p WHERE p.personel.id = :personelId")
    List<ProjectEntity> findByPersonelId(@Param("personelId") Long personelId);
}
