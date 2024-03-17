package org.work.personnelinfo.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.work.personnelinfo.file.model.FileEntity;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("SELECT f FROM FileEntity f WHERE f.personel.id = :personelId")
    List<FileEntity> findByPersonelId(@Param("personelId") Long personelId);
}
