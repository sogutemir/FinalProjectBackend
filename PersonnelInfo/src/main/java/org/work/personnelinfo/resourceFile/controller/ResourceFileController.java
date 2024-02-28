package org.work.personnelinfo.resourceFile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws FileNotFoundException {
        ResponseEntity.BodyBuilder responseBuilder = retrieveResourceFile(fileId);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId); // repeat call is okay, since it likely includes some caching mechanism
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long fileId) throws FileNotFoundException {
        ResponseEntity.BodyBuilder responseBuilder = retrieveResourceFile(fileId);
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId); // repeat call is okay, since it likely includes some caching mechanism
        return responseBuilder
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDto.getFileName() + "\"")
                .body(new ByteArrayResource(fileDto.getData()));
    }

    @GetMapping("/imageUrl/{fileId}")
    public ResponseEntity<String> getImageUrl(@PathVariable Long fileId) throws FileNotFoundException {
        String fileName = resourceFileService.getFileName(fileId);
        String fileUrl = "/images/" + fileName;
        return ResponseEntity.ok().body(fileUrl);
    }

    private ResponseEntity.BodyBuilder retrieveResourceFile(Long fileId) throws FileNotFoundException {
        ResourceFileDTO fileDto = resourceFileService.downloadFile(fileId);
        String fileType = fileDto.getFileName().substring(fileDto.getFileName().lastIndexOf('.') + 1);
        String contentType = DetermineResourceFileType.determineFileType(fileType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType));
    }
}

