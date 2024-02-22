package org.work.personnelinfo.experience.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.experience.dto.ExperienceDTO;
import org.work.personnelinfo.experience.service.ExperienceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getExperienceById(@PathVariable Long id) {
        try {
            ExperienceDTO experienceDTO = experienceService.getExperienceById(id);
            return new ResponseEntity<>(experienceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{personelId}")
    public ResponseEntity<?> getExperienceByPersonelId(@PathVariable Long personelId) {
        try {
            List<ExperienceDTO> experienceDTO = experienceService.getExperiencesByPersonelId(personelId);
            return new ResponseEntity<>(experienceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addExperience(@ModelAttribute ExperienceDTO experienceDTO) {
        try {
            ExperienceDTO createdExperience = experienceService.addExperience(experienceDTO);
            return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{experienceId}")
    public ResponseEntity<?> updateExperience(@PathVariable Long experienceId,
                                            @ModelAttribute ExperienceDTO experienceDTO) {
        try {
            ExperienceDTO updatedExperience = experienceService.updateExperience(experienceId, experienceDTO);
            return new ResponseEntity<>(updatedExperience, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{experienceId}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long experienceId) {
        try {
            experienceService.deleteExperience(experienceId);
            return new ResponseEntity<>("Experience deleted successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting staff: " + e.getMessage());
        }
    }

}
