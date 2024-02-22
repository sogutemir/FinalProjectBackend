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
    public FileDTO getFileById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return fileRepository.findById(id)
                .map(fileMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FileDTO> getFileByPersonelId(Long personelId){
        if (personelId == null) {
            throw new IllegalArgumentException("PersonelId cannot be null");
        }
        return fileRepository.findByPersonelId(personelId)
                .stream()
                .map(fileMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FileDTO addFile(FileDTO fileDTO, MultipartFile file) throws IOException {
        if (fileDTO == null || fileDTO.getPersonelId() == null) {
            throw new IllegalArgumentException("EducationDTO or personelId cannot be null");
        }
        FileEntity fileEntity = fileMapper.dtoToModel(fileDTO);

        PersonelEntity personelEntity = personelRepository.findById(fileDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + fileDTO.getPersonelId()));

        fileEntity.setPersonel(personelEntity);

        fileEntity = fileRepository.save(fileEntity);

        if(file != null && !file.isEmpty()){
            resourceFileService.uploadFile(file, fileEntity);
        }
        return fileMapper.modelToDTO(fileEntity);
    }

    @Transactional
    public FileDTO updateFile(Long fileId, FileDTO fileDTO, MultipartFile file) throws IOException {

        if(fileId == null || fileDTO == null){
            throw new IllegalArgumentException("FileId and FileDTO cannot be null");
        }

        FileEntity existingFileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));

        fileMapper.updateModel(fileDTO, existingFileEntity);

        if(file != null && !file.isEmpty()){
            if(existingFileEntity.getResourceFile() != null){
                resourceFileService.deleteFile(existingFileEntity.getResourceFile().getId());
            }
            resourceFileService.uploadFile(file, existingFileEntity);
        }

        FileEntity updatedFileEntity = fileRepository.save(existingFileEntity);

        return fileMapper.modelToDTO(updatedFileEntity);
    }

    @Transactional
    public void deleteFile(Long fileId) throws FileNotFoundException {
        if(fileId == null){
            throw new IllegalArgumentException("FileId cannot be null");
        }
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
        if(fileEntity.getResourceFile() != null){
            resourceFileService.deleteFile(fileEntity.getResourceFile().getId());
        }
        fileRepository.delete(fileEntity);
    }

}
