package org.work.personnelinfo.personel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.work.personnelinfo.personel.service.PersonelService;
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
    public PersonelDTO getPersonelById(@PathVariable Long id) {
        return personelService.getPersonelById(id);
    }

    @GetMapping("/all")
    public List<PersonelDTO> getAllPersonel() {
        return personelService.getAllPersonel();
    }

    @GetMapping("/new-personel")
    public List<PersonelDTO> getNewPersonnel() {
        return personelService.getNewPersonnel();
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonelDTO addPersonel(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @ModelAttribute PersonelDTO personelDTO) throws IOException {
        return personelService.addPersonel(personelDTO, file);
        
    }

    @PutMapping("/update/{personelId}")
    public PersonelDTO updatePersonel(@PathVariable Long personelId,
                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                      @ModelAttribute PersonelDTO personelDTO) throws IOException {
        return personelService.updatePersonel(personelId, personelDTO, file);
    }

    @PutMapping("/updateNew/{personelId}")
    public PersonelDTO updatePersonelNew(@PathVariable Long personelId,
                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                      @ModelAttribute PersonelDTO personelDTO) throws IOException {
        // The service layer will handle conversion from DTO to entity, updating logic, and saving
        return personelService.update(personelId, personelDTO, file);
    }

    @DeleteMapping("admin/delete/{personelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePersonel(@PathVariable Long personelId) throws IOException {
        personelService.deletePersonel(personelId);
    }
}