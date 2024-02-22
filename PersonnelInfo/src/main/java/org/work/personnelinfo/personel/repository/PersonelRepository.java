package org.work.personnelinfo.personel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;

public interface PersonelRepository extends JpaRepository<PersonelEntity, Long> {
}
