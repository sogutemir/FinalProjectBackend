package org.work.personnelinfo.project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.project.dto.ProjectDTO;
import org.work.personnelinfo.project.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            ProjectDTO projectDTO = projectService.getProjectById(id);
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{personelId}")
    public ResponseEntity<?> getProjectByPersonelId(@PathVariable Long personelId) {
        try {
            List<ProjectDTO> projectDTO = projectService.getProjectsByPersonelId(personelId);
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@ModelAttribute ProjectDTO projectDTO) {
        try {
            ProjectDTO createdProject = projectService.addProject(projectDTO);
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId,
                                          @ModelAttribute ProjectDTO projectDTO) {
        try {
            ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        try {
            projectService.deleteProject(projectId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Project deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
