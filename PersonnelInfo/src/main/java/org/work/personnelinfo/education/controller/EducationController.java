package org.work.personnelinfo.education.controller;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.education.dto.EducationDTO;
import org.work.personnelinfo.education.service.EducationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {

    private final EducationService educationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getEducationById(@PathVariable Long id) {
        try {
            EducationDTO educationDTO = educationService.getEducationById(id);
            return new ResponseEntity<>(educationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{personelId}")
    public ResponseEntity<?> getEducationByPersonelId(@PathVariable Long personelId) {
        try {
            List<EducationDTO> educationDTO = educationService.getEducationsByPersonelId(personelId);
            return new ResponseEntity<>(educationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPersonel(@ModelAttribute EducationDTO educationDTO) {
        try {
            EducationDTO createdEducation = educationService.addEducation(educationDTO);
            return new ResponseEntity<>(createdEducation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{educationId}")
    public ResponseEntity<?> updateEducation(@PathVariable Long educationId,
                                            @ModelAttribute EducationDTO educationDTO) {
        try {
            EducationDTO updatedEducation = educationService.updateEducation(educationId, educationDTO);
            return new ResponseEntity<>(updatedEducation, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{educationId}")
    public ResponseEntity<?> deleteEducation(@PathVariable Long educationId) {
        try {
            educationService.deleteEducation(educationId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Education deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting education: " + e.getMessage());
        }
    }


}
