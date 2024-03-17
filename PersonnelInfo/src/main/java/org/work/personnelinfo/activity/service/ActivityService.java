package org.work.personnelinfo.activity.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.mapper.ActivityMapper;
import org.work.personnelinfo.activity.model.ActivityEntity;
import org.work.personnelinfo.activity.repository.ActivityRepository;
import org.work.personnelinfo.base.service.BaseService;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class ActivityService extends BaseService<ActivityEntity, ActivityDTO, ActivityRepository> {

    private final ActivityMapper activityMapper;
    private final PersonelRepository personelRepository;

    public ActivityService(ActivityRepository activityRepository,
                           ResourceFileService resourceFileService,
                           ActivityMapper activityMapper,
                           PersonelRepository personelRepository) {
        super(activityRepository, resourceFileService);
        this.activityMapper = activityMapper;
        this.personelRepository = personelRepository;
    }

    private static final String NULL_ID_MESSAGE = "%sId cannot be null";

    @Transactional(readOnly = true)
    public ActivityDTO getActivityById(Long id) {
        validateNotNull(id, "Activity");
        return repository.findById(id)
                .map(activityMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByPersonelId(Long personelId) {
        validateNotNull(personelId, "Personel");
        return repository.findByPersonelId(personelId)
                .stream()
                .map(activityMapper::modelToDTO)
                .toList();
    }

    @Transactional
    public ActivityDTO addActivity(ActivityDTO activityDTO, MultipartFile file) throws IOException {
        validateNotNull(activityDTO, "ActivityDTO");
        validateNotNull(activityDTO.getPersonelId(), "Personel");

        ActivityEntity activityEntity = mapAndSaveActivity(activityDTO);
        handleFileUpload(file, activityEntity);

        return activityMapper.modelToDTO(activityEntity);
    }

    @Transactional
    public ActivityDTO updateActivity(Long activityId, ActivityDTO activityDTO, MultipartFile file) throws IOException {
        validateNotNull(activityId, "Activity");
        validateNotNull(activityDTO, "ActivityDTO");

        ActivityEntity existingActivityEntity = repository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));
        activityMapper.updateModel(activityDTO, existingActivityEntity);

        handleFileProcessing(file, existingActivityEntity);

        ActivityEntity updatedActivityEntity = repository.save(existingActivityEntity);
        return activityMapper.modelToDTO(updatedActivityEntity);
    }

    @Transactional
    public void deleteActivity(Long activityId) throws FileNotFoundException {
        validateNotNull(activityId, "Activity");

        ActivityEntity activityEntity = repository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));

        if (activityEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(activityEntity.getResourceFile().getId());
        }
        repository.delete(activityEntity);
    }

    private void validateNotNull(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(NULL_ID_MESSAGE, name));
        }
    }

    private ActivityEntity mapAndSaveActivity(ActivityDTO activityDTO) {
        ActivityEntity activityEntity = activityMapper.dtoToModel(activityDTO);
        PersonelEntity personelEntity = personelRepository.findById(activityDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + activityDTO.getPersonelId()));
        activityEntity.setPersonel(personelEntity);
        return repository.save(activityEntity);
    }

    private void handleFileUpload(MultipartFile file, ActivityEntity activityEntity) throws IOException {
        if (file != null) {
            resourceFileService.saveFile(file, activityEntity);
        }
    }

    private void handleFileProcessing(MultipartFile file, ActivityEntity existingActivityEntity) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (existingActivityEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(existingActivityEntity.getResourceFile().getId());
            }
            resourceFileService.saveFile(file, existingActivityEntity);
        }
    }

    @Override
    protected ActivityDTO convertToDto(ActivityEntity entity) {
        return activityMapper.modelToDTO(entity);
    }

    @Override
    protected ActivityEntity convertToEntity(ActivityDTO dto) {
        validateNotNull(dto, "ActivityDTO");
        validateNotNull(dto.getPersonelId(), "Personel");
        PersonelEntity personelEntity = personelRepository.findById(dto.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + dto.getPersonelId()));
        ActivityEntity entity = activityMapper.dtoToModel(dto);
        entity.setPersonel(personelEntity);
        return entity;
    }

    @Override
    protected void updateEntity(ActivityDTO dto, ActivityEntity entity) {
        activityMapper.updateModel(dto, entity);
    }
}
