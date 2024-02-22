package org.work.personnelinfo.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.work.personnelinfo.education.model.EducationEntity;

import java.util.List;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {

    @Query("SELECT e FROM EducationEntity e WHERE e.personel.id = ?1")
    List<EducationEntity> findByPersonelId(Long personnelId);
}
