package org.work.personnelinfo.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.education.model.EducationEntity;

import java.util.List;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {


    @Query("SELECT e FROM EducationEntity e WHERE e.personel.id = :personelId")
    List<EducationEntity> findByPersonelId(@Param("personelId") Long personelId);
}
