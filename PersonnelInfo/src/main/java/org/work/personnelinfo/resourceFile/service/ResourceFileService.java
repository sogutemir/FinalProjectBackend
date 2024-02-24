package org.work.personnelinfo.resourceFile.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.activity.model.ActivityEntity;
import org.work.personnelinfo.base.model.BaseEntity;
import org.work.personnelinfo.file.model.FileEntity;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.resourceFile.dto.ResourceFileDTO;
import org.work.personnelinfo.resourceFile.model.ResourceFileEntity;
import org.work.personnelinfo.resourceFile.repository.ResourceFileRepository;
import org.work.personnelinfo.resourceFile.utility.ResourceFileUtils;
import org.work.personnelinfo.slide.model.SlideEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResourceFileService {

    private final ResourceFileRepository fileRepository;

    @Transactional
    public String uploadFile(MultipartFile file, BaseEntity entity) throws IOException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        byte[] compressedData = ResourceFileUtils.compressBytes(file.getBytes());

        ResourceFileEntity fileEntity = ResourceFileEntity.builder()
                .name(fileName)
                .type(contentType)
                .data(compressedData)
                .build();

        associateEntityWithFile(entity, fileEntity);

        ResourceFileEntity savedFile = fileRepository.save(fileEntity);

        if (savedFile != null) {
            return "Saved file in DB with name: " + fileName;
        } else {
            return "Error: File not saved.";
        }
    }

    @Transactional(readOnly = true)
    public String getFileName(Long fileId) throws FileNotFoundException {
        return fileRepository.findById(fileId)
                .map(ResourceFileEntity::getName)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
    }


    @Transactional(readOnly = true)
    public ResourceFileDTO downloadFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity retrievedFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

        byte[] decompressedData = ResourceFileUtils.decompressBytes(retrievedFile.getData());
        String fileName = retrievedFile.getName();

        return new ResourceFileDTO(decompressedData, fileName);
    }

    private void associateEntityWithFile(BaseEntity entity, ResourceFileEntity fileEntity) {

        switch (entity.getClass().getSimpleName()) {
            case "PersonelEntity":
                PersonelEntity personelEntity = (PersonelEntity) entity;
                personelEntity.setResourceFile(fileEntity);
                break;
            case "FileEntity":
                FileEntity fileEntity1 = (FileEntity) entity;
                fileEntity1.setResourceFile(fileEntity);
                break;
            case "ActivityEntity":
                ActivityEntity activityEntity = (ActivityEntity) entity;
                activityEntity.setResourceFile(fileEntity);
                break;
            case"SlideEntity":
                SlideEntity slideEntity = (SlideEntity) entity;
                slideEntity.setResourceFile(fileEntity);
                break;
            default:
                throw new IllegalArgumentException("Entity type not supported");
        }
    }

    @Transactional
    public void deleteFile(Long fileId) throws FileNotFoundException {
        ResourceFileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
        try {
            fileRepository.delete(file);
        } catch (Exception e) {
            throw new ServiceException("Error occurred while deleting the file with id: " + fileId, e);
        }

    }


}

//
//    @Transactional(readOnly = true)
//    public byte[] downloadFile(Long fileId) throws FileNotFoundException {
//        Optional<ResourceFileEntity> retrievedFile = fileRepository.findById(fileId);
//
//        if (!retrievedFile.isPresent()) {
//            throw new FileNotFoundException("File not found with id: " + fileId);
//        }
//
//        return ResourceFileUtils.decompressBytes(retrievedFile.get().getData());
//    }
//
//

