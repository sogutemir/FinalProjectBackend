package org.work.personnelinfo.experience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.activity.model.ActivityEntity;
import org.work.personnelinfo.experience.model.ExperienceEntity;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

    @Query("SELECT e FROM ExperienceEntity e WHERE e.personel.id = :personelId")
    List<ExperienceEntity> findByPersonelId(@Param("personelId") Long personelId);
}
