package org.work.personnelinfo.slide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.personnelinfo.slide.model.SlideEntity;

public interface SlideRepository extends JpaRepository<SlideEntity, Long> {
}
