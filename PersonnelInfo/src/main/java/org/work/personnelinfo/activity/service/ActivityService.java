package org.work.personnelinfo.activity.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.mapper.ActivityMapper;
import org.work.personnelinfo.activity.model.ActivityEntity;
import org.work.personnelinfo.activity.repository.ActivityRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ResourceFileService resourceFileService;
    private final PersonelRepository personelRepository;
    private final ActivityMapper activityMapper;

    private static final String NULL_ID_MESSAGE = "%sId cannot be null";

    @Transactional(readOnly = true)
    public ActivityDTO getActivityById(Long id) {
        validateNotNull(id, "Activity");
        return activityRepository.findById(id)
                .map(activityMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByPersonelId(Long personelId) {
        validateNotNull(personelId, "Personel");
        return activityRepository.findByPersonelId(personelId)
                .stream()
                .map(activityMapper::modelToDTO)
                .collect(Collectors.toList());
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

        ActivityEntity existingActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));
        activityMapper.updateModel(activityDTO, existingActivityEntity);

        handleFileProcessing(file, existingActivityEntity);

        ActivityEntity updatedActivityEntity = activityRepository.save(existingActivityEntity);
        return activityMapper.modelToDTO(updatedActivityEntity);
    }

    @Transactional
    public void deleteActivity(Long activityId) throws FileNotFoundException {
        validateNotNull(activityId, "Activity");

        ActivityEntity activityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));

        if (activityEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(activityEntity.getResourceFile().getId());
        }
        activityRepository.delete(activityEntity);
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
        return activityRepository.save(activityEntity);
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
}
