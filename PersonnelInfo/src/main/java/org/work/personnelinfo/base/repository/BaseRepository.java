package org.work.personnelinfo.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.personnelinfo.base.model.BaseEntity;

public interface BaseRepository extends JpaRepository<BaseEntity, Long> {
}
