package org.work.personnelinfo.education.controller;

import lombok.RequiredArgsConstructor;
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
    public EducationDTO getEducationById(@PathVariable Long id) {
        return educationService.getEducationById(id);
    }

    @GetMapping("getByPersonelId/{personelId}")
    public List<EducationDTO> getEducationByPersonelId(@PathVariable Long personelId) {
        return educationService.getEducationsByPersonelId(personelId);
    }

    @PostMapping("/add")
    public EducationDTO addPersonel(@ModelAttribute EducationDTO educationDTO) {
        return educationService.addEducation(educationDTO);
    }

    @PutMapping("/update/{educationId}")
    public EducationDTO updateEducation(@PathVariable Long educationId,
                                        @ModelAttribute EducationDTO educationDTO) {
        return educationService.updateEducation(educationId, educationDTO);
    }

    @DeleteMapping("/delete/{educationId}")
    public void deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducation(educationId);
    }
}