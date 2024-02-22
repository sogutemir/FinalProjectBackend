package org.work.personnelinfo.file.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.file.dto.FileDTO;
import org.work.personnelinfo.file.service.FileService;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFileById(@PathVariable Long id) {
        try {
            FileDTO fileDTO = fileService.getFileById(id);
            return new ResponseEntity<>(fileDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

@GetMapping("/{personelId}")
public ResponseEntity<?> getFilesByPersonelId(@PathVariable(required = false) Long personelId) {
    try {
        List<FileDTO> fileDTO = fileService.getFileByPersonelId(personelId);
        if (fileDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found for personelId: " + personelId);
        }
        return new ResponseEntity<>(fileDTO, HttpStatus.OK);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}

    @PostMapping("/add")
    public ResponseEntity<?> addFile(@RequestParam(value = "file") MultipartFile file,
                                     @ModelAttribute FileDTO fileDTO) {
        try {
            FileDTO createdFile = fileService.addFile(fileDTO, file);
            return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{fileId}")
    public ResponseEntity<?> updateFile(@PathVariable Long fileId,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @ModelAttribute FileDTO fileDTO) {
        try {
            FileDTO updatedFile = fileService.updateFile(fileId, fileDTO, file);
            return new ResponseEntity<>(updatedFile, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting staff: " + e.getMessage());
        }
    }
}
