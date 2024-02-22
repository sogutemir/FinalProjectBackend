package org.work.personnelinfo.experience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.work.personnelinfo.experience.model.ExperienceEntity;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

    @Query("SELECT e FROM ExperienceEntity e WHERE e.personel.id = ?1")
    List<ExperienceEntity> findByPersonelId(Long personnelId);
}
