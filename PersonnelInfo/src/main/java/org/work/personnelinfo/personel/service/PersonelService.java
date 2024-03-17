package org.work.personnelinfo.personel.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.base.service.BaseService;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.mapper.PersonelMapper;
import org.work.personnelinfo.personel.model.PersonelProjection;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class PersonelService extends BaseService<PersonelEntity, PersonelDTO, PersonelRepository> {

    private final PersonelMapper personelMapper;
    private static final String PERSONEL_NOT_FOUND_WITH_ID = "Personel not found with id: ";

    public PersonelService(PersonelRepository personelRepository, ResourceFileService resourceFileService, PersonelMapper personelMapper) {
        super(personelRepository, resourceFileService);
        this.personelMapper = personelMapper;
    }

    @Override
    protected PersonelDTO convertToDto(PersonelEntity entity) {
        return personelMapper.modelToDTO(entity);
    }

    @Override
    protected PersonelEntity convertToEntity(PersonelDTO dto) {
        return personelMapper.dtoToModel(dto);
    }

    @Override
    protected void updateEntity(PersonelDTO dto, PersonelEntity entity) {
        personelMapper.updateModel(dto, entity);
    }

    private enum ProcessType {
        UPLOAD, DELETE
    }

    @Transactional(readOnly = true)
    public PersonelDTO getPersonelById(Long id) {
        return personelMapper.modelToDTO(getPersonelEntityById(id));
    }


    @Transactional(readOnly = true)
    public PersonelProjection getFilteredPersonelById(Long id) {
        return repository.findProjectionById(id);
    }

    @Transactional(readOnly = true)
    public List<PersonelDTO> getAllPersonel() {
        List<PersonelEntity> personelEntities = repository.findAll();
        return personelEntities.stream()
                .map(personelMapper::modelToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PersonelDTO> getNewPersonnel() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<PersonelEntity> personelEntities =
                repository.findPersonnelWhoStartedWithinTheLastMonth(oneMonthAgo);

        return  personelEntities.stream()
                .map(personelMapper::modelToDTO)
                .toList();
    }

    @Transactional
    public PersonelDTO addPersonel(PersonelDTO personelDTO, MultipartFile file) throws IOException {
        PersonelEntity newPersonelEntity = personelMapper.dtoToModel(Objects.requireNonNull(personelDTO, "PersonelDTO cannot be null"));
        newPersonelEntity = repository.save(newPersonelEntity);
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
        repository.flush();

        PersonelEntity updatedPersonelEntity = repository.save(existingPersonelEntity);
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
        repository.delete(personelEntity);
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(PERSONEL_NOT_FOUND_WITH_ID + id));
    }
}
