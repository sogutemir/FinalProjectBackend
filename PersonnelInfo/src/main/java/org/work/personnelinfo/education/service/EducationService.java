package org.work.personnelinfo.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.personnelinfo.education.dto.EducationDTO;
import org.work.personnelinfo.education.mapper.EducationMapper;
import org.work.personnelinfo.education.model.EducationEntity;
import org.work.personnelinfo.education.repository.EducationRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final PersonelRepository personelRepository;


    @Transactional(readOnly = true)
    public EducationDTO getEducationById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return educationRepository.findById(id)
                .map(educationMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Education not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<EducationDTO> getEducationsByPersonelId(Long personelId) {
        if (personelId == null) {
            throw new IllegalArgumentException("PersonelId cannot be null");
        }
        return educationRepository.findByPersonelId(personelId)
                .stream()
                .map(educationMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationDTO addEducation(EducationDTO educationDTO) {
        if (educationDTO == null || educationDTO.getPersonelId() == null) {
            throw new IllegalArgumentException("EducationDTO or personelId cannot be null");
        }

        EducationEntity educationEntity = educationMapper.dtoToModel(educationDTO);
        PersonelEntity personelEntity = personelRepository.findById(educationDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + educationDTO.getPersonelId()));

        educationEntity.setPersonel(personelEntity);
        educationEntity = educationRepository.save(educationEntity);

        return educationMapper.modelToDTO(educationEntity);
    }

    @Transactional
    public EducationDTO updateEducation(Long educationId, EducationDTO educationDTO) {
        if (educationId == null || educationDTO == null) {
            throw new IllegalArgumentException("EducationId or EducationDTO cannot be null");
        }

        EducationEntity existingEducationEntity = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Education not found with id: " + educationId));

        educationMapper.updateModel(educationDTO, existingEducationEntity);

        return educationMapper.modelToDTO(existingEducationEntity);
    }

    @Transactional
    public void deleteEducation(Long educationId){
        if (educationId == null) {
            throw new IllegalArgumentException("EducationId cannot be null");
        }

        EducationEntity educationEntity = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + educationId));
        educationRepository.delete(educationEntity);
    }


}
