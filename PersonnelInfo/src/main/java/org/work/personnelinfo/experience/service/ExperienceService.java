package org.work.personnelinfo.experience.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.personnelinfo.experience.dto.ExperienceDTO;
import org.work.personnelinfo.experience.mapper.ExperienceMapper;
import org.work.personnelinfo.experience.model.ExperienceEntity;
import org.work.personnelinfo.experience.repository.ExperienceRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final PersonelRepository personelRepository;
    private final ExperienceMapper experienceMapper;

    @Transactional(readOnly = true)
    public ExperienceDTO getExperienceById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return experienceRepository.findById(id)
                .map(experienceMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ExperienceDTO> getExperiencesByPersonelId(Long personelId) {
        if (personelId == null) {
            throw new IllegalArgumentException("PersonelId cannot be null");
        }
        return experienceRepository.findByPersonelId(personelId)
                .stream()
                .map(experienceMapper::modelToDTO)
                .toList();
    }


    @Transactional
    public ExperienceDTO addExperience(ExperienceDTO experienceDTO){
        if(experienceDTO == null || experienceDTO.getPersonelId() == null){
            throw new IllegalArgumentException("ExperienceDTO or personelId cannot be null");
        }

        ExperienceEntity experienceEntity = experienceMapper.dtoToModel(experienceDTO);
        PersonelEntity personelEntity = personelRepository.findById(experienceDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + experienceDTO.getPersonelId()));

        experienceEntity.setPersonel(personelEntity);
        experienceEntity = experienceRepository.save(experienceEntity);

        return experienceMapper.modelToDTO(experienceEntity);
    }

    @Transactional
    public ExperienceDTO updateExperience(Long experienceId, ExperienceDTO experienceDTO){
        if (experienceId == null || experienceDTO == null) {
            throw new IllegalArgumentException("ExperienceId or ExperienceDTO cannot be null");
        }

        ExperienceEntity existingExperienceEntity = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + experienceId));

        experienceMapper.updateModel(experienceDTO, existingExperienceEntity);

        return experienceMapper.modelToDTO(existingExperienceEntity);
    }

    @Transactional
    public void deleteExperience(Long experienceId){
        if (experienceId == null) {
            throw new IllegalArgumentException("ExperienceId cannot be null");
        }

        ExperienceEntity experienceEntity = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + experienceId));
        experienceRepository.delete(experienceEntity);
    }


}
