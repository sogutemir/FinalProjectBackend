package org.work.personnelinfo.base.service;

import org.work.personnelinfo.base.model.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import java.io.IOException;
import java.util.Objects;

public abstract class BaseService<T extends BaseEntity, D, R extends JpaRepository<T, Long>> {
    protected final R repository;
    protected final ResourceFileService resourceFileService;

    public BaseService(R repository, ResourceFileService resourceFileService) {
        this.repository = repository;
        this.resourceFileService = resourceFileService;
    }

    // Abstract methods to be implemented by subclasses for specific type mappings
    protected abstract D convertToDto(T entity);
    protected abstract T convertToEntity(D dto);
    protected abstract void updateEntity(D dto, T entity);

    @Transactional
    public D update(Long entityId, D dto, MultipartFile file) throws IOException {
        Objects.requireNonNull(entityId, "Entity ID cannot be null");
        Objects.requireNonNull(dto, "DTO cannot be null");

        T existingEntity = repository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        updateEntity(dto, existingEntity);
        handleFile(existingEntity, file);
        repository.flush();

        T updatedEntity = repository.save(existingEntity);
        return convertToDto(updatedEntity);
    }

    private void handleFile(T existingEntity, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (existingEntity.getResourceFile() != null) {
                Long oldFileId = existingEntity.getResourceFile().getId();
                resourceFileService.updateFile(oldFileId, file);
            } else {
                resourceFileService.saveFile(file, existingEntity);
            }
        }
    }
}