package org.work.personnelinfo.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.file.dto.FileDTO;
import org.work.personnelinfo.file.service.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public FileDTO getFileById(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @GetMapping("getByPersonelId/{personelId}")
    public List<FileDTO> getFilesByPersonelId(@PathVariable(required = false) Long personelId) {
        return fileService.getFileByPersonelId(personelId);
    }

    @PostMapping("/add")
    public FileDTO addFile(@RequestParam(value = "file") MultipartFile file,
                           @ModelAttribute FileDTO fileDTO) throws IOException {
        return fileService.addFile(fileDTO, file);
    }

    @PutMapping("/update/{fileId}")
    public FileDTO updateFile(@PathVariable Long fileId,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @ModelAttribute FileDTO fileDTO) throws IOException {
        return fileService.updateFile(fileId, fileDTO, file);
    }

    @DeleteMapping("/delete/{fileId}")
    public void deleteFile(@PathVariable Long fileId) throws FileNotFoundException {
        fileService.deleteFile(fileId);
    }
}