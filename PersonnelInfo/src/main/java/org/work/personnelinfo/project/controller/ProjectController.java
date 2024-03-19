package org.work.personnelinfo.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.project.dto.ProjectDTO;
import org.work.personnelinfo.project.dto.ProjectUpdateRequestDTO;
import org.work.personnelinfo.project.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectDTO getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("getByPersonelId/{personelId}")
    public List<ProjectDTO> getProjectByPersonelId(@PathVariable Long personelId) {
        return projectService.getProjectsByPersonelId(personelId);
    }

    @PostMapping("/add")
    public ProjectDTO addProject(@ModelAttribute ProjectDTO projectDTO) {
        return projectService.addProject(projectDTO);
    }

    @PutMapping("/update/{projectId}")
    public ProjectDTO updateProject(@PathVariable Long projectId, @ModelAttribute ProjectDTO projectDTO){
        ProjectUpdateRequestDTO updateRequest = new ProjectUpdateRequestDTO(projectId, projectDTO);
        return projectService.updateProject(updateRequest);
    }

    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }
}