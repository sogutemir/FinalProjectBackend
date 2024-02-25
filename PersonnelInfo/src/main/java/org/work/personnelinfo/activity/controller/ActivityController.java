package org.work.personnelinfo.activity.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.service.ActivityService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getActivityById(@PathVariable Long id){
        try{
            ActivityDTO activityDTO = activityService.getActivityById(id);
            return new ResponseEntity<>(activityDTO, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("getByPersonelId/{personelId}")
    public ResponseEntity<?> getActivitiesByPersonelId(@PathVariable Long personelId){
        try{
            List<ActivityDTO> activityDTO = activityService.getActivitiesByPersonelId(personelId);
            if(activityDTO.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activities found for personelId: " + personelId);
            }
            return new ResponseEntity<>(activityDTO, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addActivity(@RequestParam(value = "file", required = false) MultipartFile file,
                                        @ModelAttribute ActivityDTO activityDTO){
        try{
            ActivityDTO createdActivity = activityService.addActivity(activityDTO, file);
            return new ResponseEntity<>(createdActivity, HttpStatus.CREATED);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding staff: " + e.getMessage());
        }
    }

    @PutMapping("/update/{activityId}")
    public ResponseEntity<?> updateActivity(@PathVariable Long activityId,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @ModelAttribute ActivityDTO activityDTO){
        try{
            ActivityDTO updatedActivity = activityService.updateActivity(activityId, activityDTO, file);
            return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error loading the file: " + e.getMessage());
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{activityId}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long activityId){
        try{
            activityService.deleteActivity(activityId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Activity deleted successfully");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting staff: " + e.getMessage());
        }
    }

}
