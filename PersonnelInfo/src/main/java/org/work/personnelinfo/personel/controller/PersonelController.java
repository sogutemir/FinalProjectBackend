package org.work.personnelinfo.personel.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.personel.service.PersonelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.personel.dto.PersonelDTO;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/personel")
public class PersonelController {

    private final PersonelService personelService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonelById(@PathVariable Long id) {
        try {
            PersonelDTO personelDTO = personelService.getPersonelById(id);
            return new ResponseEntity<>(personelDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPersonel() {
        try {
            List<PersonelDTO> personelDTO = personelService.getPersonelAll();
            return new ResponseEntity<>(personelDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPersonel(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @ModelAttribute PersonelDTO personelDTO) {
        try {
            PersonelDTO createdPersonel = personelService.addPersonel(personelDTO, file);
            return new ResponseEntity<>(createdPersonel, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }
    @PutMapping("/update/{personelId}")
    public ResponseEntity<?> updatePersonel(@PathVariable Long personelId,
                                            @RequestParam(value = "file", required = false) MultipartFile file,
                                            @ModelAttribute PersonelDTO personelDTO) {
        try {
            PersonelDTO updatedPersonel = personelService.updatePersonel(personelId, personelDTO, file);
            return new ResponseEntity<>(updatedPersonel, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{personelId}")
    public ResponseEntity<?> deletePersonel(@PathVariable Long personelId) {
        try {
            personelService.deletePersonel(personelId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Personel deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting staff: " + e.getMessage());
        }
    }


}
