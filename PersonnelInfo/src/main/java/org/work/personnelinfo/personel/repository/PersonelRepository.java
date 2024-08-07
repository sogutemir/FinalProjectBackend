package org.work.personnelinfo.personel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.model.PersonelProjection;
import org.work.personnelinfo.personel.model.PersonelUserProjection;

import java.time.LocalDate;
import java.util.List;

public interface PersonelRepository extends JpaRepository<PersonelEntity, Long> {

    @Query("SELECT p FROM PersonelEntity p WHERE p.teamName = :teamName")
    List<PersonelEntity> findByTeamName(@Param("teamName") String teamName);

    @Query("SELECT p FROM PersonelEntity p WHERE p.startDateOfEmployment >= :fromDate")
    List<PersonelEntity> findPersonnelWhoStartedWithinTheLastMonth(@Param("fromDate") LocalDate fromDate);

    @Query("SELECT p.teamName FROM PersonelEntity p WHERE p.personel_user.username = :username")
    String findTeamNameByUsername(@Param("username") String username);

    @Query("SELECT p FROM PersonelEntity p WHERE p.personel_user.username = :username")
    PersonelEntity findUserByUsername(@Param("username") String username);

    @Query("SELECT p FROM PersonelEntity p WHERE p.id = :id")
    PersonelProjection findProjectionById(@Param("id") Long id);

    @Query("SELECT p.name as name, p.surname as surname, u.roles as roles, u.id as userId " +
            "FROM PersonelEntity p " +
            "INNER JOIN p.personel_user u ")
    List<PersonelUserProjection> findAllProjected();





}
