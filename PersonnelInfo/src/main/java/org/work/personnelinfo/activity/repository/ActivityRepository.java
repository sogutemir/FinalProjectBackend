package org.work.personnelinfo.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.activity.model.ActivityEntity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    @Query("SELECT a FROM ActivityEntity a WHERE a.personel.id = :personelId")
    List<ActivityEntity> findByPersonelId(@Param("personelId") Long personelId);
}
