package org.work.personnelinfo.personel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.model.PersonelProjection;

import java.time.LocalDate;
import java.util.List;

public interface PersonelRepository extends JpaRepository<PersonelEntity, Long> {

    @Query("SELECT p FROM PersonelEntity p WHERE p.teamName = ?1")
    List<PersonelEntity> findByTeamName(String teamName);

    @Query("SELECT p FROM PersonelEntity p WHERE p.id = :id")
    PersonelProjection findProjectionById(@Param("id") Long id);

    @Query("SELECT p FROM PersonelEntity p WHERE p.startDateOfEmployment >= :fromDate")
    List<PersonelEntity> findPersonnelWhoStartedWithinTheLastMonth(@Param("fromDate") LocalDate fromDate);
}
