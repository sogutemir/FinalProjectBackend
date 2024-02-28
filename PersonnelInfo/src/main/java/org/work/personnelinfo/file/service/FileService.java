package org.work.personnelinfo.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.file.dto.FileDTO;
import org.work.personnelinfo.file.mapper.FileMapper;
import org.work.personnelinfo.file.model.FileEntity;
import org.work.personnelinfo.file.repository.FileRepository;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ResourceFileService resourceFileService;
    private final PersonelRepository personelRepository;
    private final FileMapper fileMapper;

    @Transactional(readOnly = true)
    public FileDTO fetchFileById(Long id) {
        validateId(id);
        return fileRepository.findById(id)
                .map(fileMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FileDTO> fetchFilesByPersonelId(Long personelId) {
        validateId(personelId);
        return fileRepository.findByPersonelId(personelId)
                .stream()
                .map(fileMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FileDTO addNewFile(FileDTO fileDTO, MultipartFile file) throws IOException {
        validateInputs(fileDTO, fileDTO.getPersonelId());

        FileEntity fileEntity = instantiateFileEntity(fileDTO);

        processUpload(file, fileEntity);

        return fileMapper.modelToDTO(fileEntity);
    }

    @Transactional
    public FileDTO updateExistingFile(Long fileId, FileDTO fileDTO, MultipartFile file) throws IOException {
        validateInputs(fileId, fileDTO);

        FileEntity updatedFileEntity = applyUpdates(fileId, fileDTO, file);

        return fileMapper.modelToDTO(updatedFileEntity);
    }

    @Transactional
    public void removeFile(Long fileId) throws FileNotFoundException {
        validateId(fileId);
        FileEntity fileEntity = validateExistingFile(fileId);
        if (fileEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(fileEntity.getResourceFile().getId());
        }
        fileRepository.delete(fileEntity);
    }

   
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    private void validateInputs(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("Inputs cannot be null");
            }
        }
    }

    private FileEntity instantiateFileEntity(FileDTO fileDTO) {
        FileEntity fileEntity = fileMapper.dtoToModel(fileDTO);

        PersonelEntity personelEntity = personelRepository.findById(fileDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + fileDTO.getPersonelId()));

        fileEntity.setPersonel(personelEntity);
        fileRepository.save(fileEntity);

        return fileEntity;
    }

    private void processUpload(MultipartFile file, FileEntity fileEntity) throws IOException {
        if (file != null && !file.isEmpty()) {
            resourceFileService.saveFile(file, fileEntity);
        }
    }

    private FileEntity validateExistingFile(Long fileId) throws FileNotFoundException {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
    }

    private FileEntity applyUpdates(Long fileId, FileDTO fileDTO, MultipartFile file) throws IOException {
        FileEntity existingFileEntity = validateExistingFile(fileId);
        fileMapper.updateModel(fileDTO, existingFileEntity);
        processUpload(file, existingFileEntity);

        return fileRepository.save(existingFileEntity);
    }
}
