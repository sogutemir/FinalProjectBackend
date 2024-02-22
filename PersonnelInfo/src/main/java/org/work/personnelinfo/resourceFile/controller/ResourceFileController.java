package org.work.personnelinfo.resourceFile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.work.personnelinfo.resourceFile.dto.ResourceFileDTO;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import org.work.personnelinfo.resourceFile.utility.DetermineResourceFileType;

import java.io.FileNotFoundException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/resourceFile")
public class ResourceFileController {

    private final ResourceFileService resourceFileService;


    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        try {
            ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
            byte[] data = fileDto.getData();
            String fileName = fileDto.getFileName();
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
            String contentType = DetermineResourceFileType.determineFileType(fileType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(data);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long fileId) {
        try {
            ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
            byte[] data = fileDto.getData();
            String fileName = fileDto.getFileName();
            String contentType = DetermineResourceFileType.determineFileType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(data));
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/imageUrl/{fileId}")
    public ResponseEntity<String> getImageUrl(@PathVariable Long fileId) {
        try {
            String fileName = resourceFileService.getFileName(fileId);
            String fileUrl = "/images/" + fileName; // Sunucunuzun statik dosyalara erişim yolu
            return ResponseEntity.ok().body(fileUrl);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }


}


//    @GetMapping("/download/{fileId}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
//        try {
//            byte[] data = resourceFileService.downloadFile(fileId);
//            String fileName = resourceFileService.getFileName(fileId);
//            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
//            String contentType = DetermineResourceFileType.determineFileType(fileType);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                    .body(data);
//        } catch (FileNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/image/{fileId}")
//    public ResponseEntity<Resource> serveImage(@PathVariable Long fileId) {
//        try {
//            byte[] data = resourceFileService.downloadFile(fileId);
//            String fileName = resourceFileService.getFileName(fileId);
//            String contentType = DetermineResourceFileType.determineFileType(fileName);
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
//                    .body(new ByteArrayResource(data));
//        } catch (FileNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
