package org.work.personnelinfo.personel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.util.List;

public interface PersonelRepository extends JpaRepository<PersonelEntity, Long> {

    @Query("SELECT p FROM PersonelEntity p WHERE p.teamName = ?1")
    List<PersonelEntity> findByTeamName(String teamName);
}
