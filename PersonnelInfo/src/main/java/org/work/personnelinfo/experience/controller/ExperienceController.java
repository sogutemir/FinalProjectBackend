package org.work.personnelinfo.experience.controller;

import lombok.RequiredArgsConstructor;
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
    public ExperienceDTO getExperienceById(@PathVariable Long id) {
        return experienceService.getExperienceById(id);
    }

    @GetMapping("/{personelId}")
    public List<ExperienceDTO> getExperienceByPersonelId(@PathVariable Long personelId) {
        return experienceService.getExperiencesByPersonelId(personelId);
    }

    @PostMapping("/add")
    public ExperienceDTO addExperience(@ModelAttribute ExperienceDTO experienceDTO) {
        return experienceService.addExperience(experienceDTO);
    }

    @PutMapping("/update/{experienceId}")
    public ExperienceDTO updateExperience(@PathVariable Long experienceId,
                                          @ModelAttribute ExperienceDTO experienceDTO) {
        return experienceService.updateExperience(experienceId, experienceDTO);
    }

    @DeleteMapping("/delete/{experienceId}")
    public void deleteExperience(@PathVariable Long experienceId) {
        experienceService.deleteExperience(experienceId);
    }
}