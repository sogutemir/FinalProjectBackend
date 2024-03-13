package org.work.personnelinfo.slide.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.slide.dto.SlideDTO;
import org.work.personnelinfo.slide.service.SlideService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slide")
public class SlideController {

    private final SlideService slideService;

    @GetMapping("/{id}")
    public SlideDTO getSlideById(@PathVariable Long id){
        return slideService.getSlideById(id);
    }

    @GetMapping("/getAll")
    public List<SlideDTO> getAllSlide(){
        return slideService.getAllSlides();
    }

    @PostMapping("/add")
    public SlideDTO addSlide(@RequestParam(value = "file", required = false) MultipartFile file,
                             @ModelAttribute SlideDTO slideDTO) throws IOException {
        return slideService.addSlide(file, slideDTO);
    }

    @PutMapping("/update/{slideId}")
    public SlideDTO updateActivity(@PathVariable Long slideId,
                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                   @ModelAttribute SlideDTO slideDTO) throws IOException {
        return slideService.updateSlide(slideId, slideDTO, file);
    }

    @PutMapping("/updateNew/{slideId}")
    public SlideDTO updateNewActivity(@PathVariable Long slideId,
                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                   @ModelAttribute SlideDTO slideDTO) throws IOException {
        return slideService.update(slideId, slideDTO, file);
    }

    @DeleteMapping("/delete/{slideId}")
    public void deleteActivity(@PathVariable Long slideId) throws FileNotFoundException {
        slideService.deleteSlide(slideId);
    }
}