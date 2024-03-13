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
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonelService {
    private static final String PERSONEL_NOT_FOUND_WITH_ID = "Personel not found with id: ";
    private final PersonelRepository personelRepository;
    private final PersonelMapper personelMapper;
    private final ResourceFileService resourceFileService;

    private enum ProcessType {
        UPLOAD, DELETE
    }

    @Transactional(readOnly = true)
    public PersonelDTO getPersonelById(Long id) {
        return personelMapper.modelToDTO(getPersonelEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<PersonelDTO> getAllPersonel() {
        List<PersonelEntity> personelEntities = personelRepository.findAll();
        return personelEntities.stream()
                .map(personelMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PersonelDTO> getNewPersonnel() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<PersonelEntity> personelEntities =
                personelRepository.findPersonnelWhoStartedWithinTheLastMonth(oneMonthAgo);

        return  personelEntities.stream()
                .map(personelMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PersonelDTO addPersonel(PersonelDTO personelDTO, MultipartFile file) throws IOException {
        PersonelEntity newPersonelEntity = personelMapper.dtoToModel(Objects.requireNonNull(personelDTO, "PersonelDTO cannot be null"));
        newPersonelEntity = personelRepository.save(newPersonelEntity);
        handleFile(ProcessType.UPLOAD, file, newPersonelEntity);
        return personelMapper.modelToDTO(newPersonelEntity);
    }

    @Transactional
    public PersonelDTO updatePersonel(Long personelId, PersonelDTO personelDTO, MultipartFile file) throws IOException {
        Objects.requireNonNull(personelId, "PersonelId cannot be null");
        Objects.requireNonNull(personelDTO, "PersonelDTO cannot be null");

        PersonelEntity existingPersonelEntity = getPersonelEntityById(personelId);
        personelMapper.updateModel(personelDTO, existingPersonelEntity);

        handleOldFileIfExistsAndNewFile(existingPersonelEntity, file);
        personelRepository.flush();

        PersonelEntity updatedPersonelEntity = personelRepository.save(existingPersonelEntity);
        return personelMapper.modelToDTO(updatedPersonelEntity);
    }

    private void handleOldFileIfExistsAndNewFile(PersonelEntity existingPersonelEntity, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (existingPersonelEntity.getResourceFile() != null) {
                Long oldFileId = existingPersonelEntity.getResourceFile().getId();
                resourceFileService.updateFile(oldFileId, file);
            } else {
                resourceFileService.saveFile(file, existingPersonelEntity);
            }
        }
    }

    @Transactional
    public void deletePersonel(Long personelId) throws IOException {
        PersonelEntity personelEntity = getPersonelEntityById(Objects.requireNonNull(personelId, "PersonelId cannot be null"));
        handleFile(ProcessType.DELETE, null, personelEntity);
        personelRepository.delete(personelEntity);
    }

    private void handleFile(ProcessType processType, MultipartFile file, PersonelEntity personelEntity) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (processType == ProcessType.DELETE && personelEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(personelEntity.getResourceFile().getId());
            }
            resourceFileService.saveFile(file, personelEntity);
        }
    }

    private PersonelEntity getPersonelEntityById(Long id) {
        return personelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PERSONEL_NOT_FOUND_WITH_ID + id));
    }
}
