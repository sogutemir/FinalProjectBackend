package org.work.personnelinfo.personel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.mapper.PersonelMapper;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PersonelService {

    private final PersonelRepository personelRepository;
    private final PersonelMapper personelMapper;
    private final ResourceFileService resourceFileService;

    @Transactional(readOnly = true)
    public PersonelDTO getPersonelById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return personelRepository.findById(id)
                .map(personelMapper::modelToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Personel not found with id: " + id));
    }


    @Transactional
    public PersonelDTO addPersonel(PersonelDTO personelDTO, MultipartFile file) throws IOException {
        if (personelDTO == null ) {
            throw new IllegalArgumentException("PersonelDTO cannot be null");
        }

        PersonelEntity personelEntity = personelMapper.dtoToModel(personelDTO);

        personelEntity = personelRepository.save(personelEntity);

        if (file != null && !file.isEmpty()) {
            resourceFileService.uploadFile(file, personelEntity);
        }

        return personelMapper.modelToDTO(personelEntity);
    }

    @Transactional
    public PersonelDTO updatePersonel(Long personelId, PersonelDTO personelDTO, MultipartFile file) throws IOException {
        if (personelId == null || personelDTO == null) {
            throw new IllegalArgumentException("PersonelId or PersonelDTO cannot be null");
        }

        PersonelEntity existingPersonelEntity = personelRepository.findById(personelId)
                .orElseThrow(() -> new EntityNotFoundException("Personel not found with id: " + personelId));

        personelMapper.updateModel(personelDTO, existingPersonelEntity);

        if (file != null && !file.isEmpty()) {
            if (existingPersonelEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(existingPersonelEntity.getResourceFile().getId());
            }
            resourceFileService.uploadFile(file, existingPersonelEntity);
        }

        PersonelEntity updatedPersonelEntity = personelRepository.save(existingPersonelEntity);

        return personelMapper.modelToDTO(updatedPersonelEntity);
    }

    @Transactional
    public void deletePersonel(Long personelId) throws FileNotFoundException {
        if (personelId == null) {
            throw new IllegalArgumentException("PersonelId cannot be null");
        }

        PersonelEntity personelEntity = personelRepository.findById(personelId)
                .orElseThrow(() -> new EntityNotFoundException("Personel not found with id: " + personelId));

        if (personelEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(personelEntity.getResourceFile().getId());
        }

        personelRepository.delete(personelEntity);
    }



}
