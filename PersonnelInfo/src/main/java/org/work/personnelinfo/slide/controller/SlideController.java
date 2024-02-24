package org.work.personnelinfo.slide.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.slide.dto.SlideDTO;
import org.work.personnelinfo.slide.service.SlideService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slide")
public class SlideController {

    private final SlideService slideService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSlideById(@PathVariable Long id){
        try{
            SlideDTO slideDTO = slideService.getSlideById(id);
            return new ResponseEntity<>(slideDTO, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSlide(){
        try{
            List<SlideDTO> slideDTO = slideService.getAllSlides();
            if(slideDTO.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activities found");
            }
            return new ResponseEntity<>(slideDTO, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSlide(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @ModelAttribute SlideDTO slideDTO){
        try{
            SlideDTO createdSlide = slideService.addSlide(file, slideDTO);
            return new ResponseEntity<>(createdSlide, HttpStatus.CREATED);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{slideId}")
    public ResponseEntity<?> updateActivity(@PathVariable Long slideId,
                                            @RequestParam(value = "file", required = false) MultipartFile file,
                                            @ModelAttribute SlideDTO slideDTO){
        try{
            SlideDTO updatedSlide = slideService.updateSlide(slideId, slideDTO, file);
            return new ResponseEntity<>(updatedSlide, HttpStatus.OK);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{slideId}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long slideId){
        try{
            slideService.deleteSlide(slideId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Activity deleted successfully");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting staff: " + e.getMessage());
        }
    }
}
