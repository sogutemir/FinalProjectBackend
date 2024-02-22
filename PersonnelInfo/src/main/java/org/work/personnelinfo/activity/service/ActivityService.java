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

    @Transactional(readOnly = true)
    public ActivityDTO getActivityById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return activityRepository.findById(id)
                .map(activityMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByPersonelId(Long personelId) {
        if (personelId == null) {
            throw new IllegalArgumentException("PersonelId cannot be null");
        }
        return activityRepository.findByPersonelId(personelId)
                .stream()
                .map(activityMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActivityDTO addActivity(ActivityDTO activityDTO, MultipartFile file) throws IOException {
        if (activityDTO == null || activityDTO.getPersonelId() == null) {
            throw new IllegalArgumentException("ActivityDTO or personelId cannot be null");
        }

        ActivityEntity activityEntity = activityMapper.dtoToModel(activityDTO);

        PersonelEntity personelEntity = personelRepository.findById(activityDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + activityDTO.getPersonelId()));

        activityEntity.setPersonel(personelEntity);

        activityEntity = activityRepository.save(activityEntity);

        if (file != null){
            resourceFileService.uploadFile(file, activityEntity);
        }

        return activityMapper.modelToDTO(activityEntity);
    }

    @Transactional
    public ActivityDTO updateActivity(Long activityId, ActivityDTO activityDTO, MultipartFile file) throws IOException {
        if (activityId == null || activityDTO == null) {
            throw new IllegalArgumentException("ActivityId or ActivityDTO cannot be null");
        }

        ActivityEntity existingActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));

        activityMapper.updateModel(activityDTO, existingActivityEntity);

        if (file != null && !file.isEmpty()) {
            if (existingActivityEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(existingActivityEntity.getResourceFile().getId());
            }
            resourceFileService.uploadFile(file, existingActivityEntity);
        }

        ActivityEntity updatedActivityEntity = activityRepository.save(existingActivityEntity);

        return activityMapper.modelToDTO(updatedActivityEntity);
    }

    @Transactional
    public void deleteActivity(Long activityId) throws FileNotFoundException {
        if (activityId == null) {
            throw new IllegalArgumentException("ActivityId cannot be null");
        }

        ActivityEntity activityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));

        if(activityEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(activityEntity.getResourceFile().getId());
        }

        activityRepository.delete(activityEntity);
    }
}
