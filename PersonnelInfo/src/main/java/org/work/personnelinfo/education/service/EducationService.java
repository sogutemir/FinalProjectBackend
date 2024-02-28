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
        assertNonNull(id, "Id cannot be null");
        return educationMapper.modelToDTO(getEducationFromDB(id));
    }

    @Transactional(readOnly = true)
    public List<EducationDTO> getEducationsByPersonelId(Long personelId) {
        assertNonNull(personelId, "PersonelId cannot be null");
        return educationRepository.findByPersonelId(personelId)
                .stream()
                .map(educationMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationDTO addEducation(EducationDTO educationDTO) {
        assertNonNull(educationDTO, "EducationDTO cannot be null");
        assertNonNull(educationDTO.getPersonelId(), "personalId cannot be null");
        EducationEntity educationEntity = educationMapper.dtoToModel(educationDTO);
        PersonelEntity personelEntity = getPersonelFromDB(educationDTO.getPersonelId());
        educationEntity.setPersonel(personelEntity);
        educationEntity = educationRepository.save(educationEntity);
        return educationMapper.modelToDTO(educationEntity);
    }

    @Transactional
    public EducationDTO updateEducation(Long educationId, EducationDTO educationDTO) {
        assertNonNull(educationId, "EducationId cannot be null");
        assertNonNull(educationDTO, "EducationDTO cannot be null");
        EducationEntity existingEducationEntity = getEducationFromDB(educationId);
        educationMapper.updateModel(educationDTO, existingEducationEntity);
        return educationMapper.modelToDTO(existingEducationEntity);
    }

    @Transactional
    public void deleteEducation(Long educationId) {
        assertNonNull(educationId, "EducationId cannot be null");
        EducationEntity educationEntity = getEducationFromDB(educationId);
        educationRepository.delete(educationEntity);
    }

    private void assertNonNull(Object object, String message) {
        if (object == null) throw new IllegalArgumentException(message);
    }

    private EducationEntity getEducationFromDB(Long id) {
        return educationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Education not found with id: " + id));
    }

    private PersonelEntity getPersonelFromDB(Long id) {
        return personelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + id));
    }
}
