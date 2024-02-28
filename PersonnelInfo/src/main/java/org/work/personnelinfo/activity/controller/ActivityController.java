package org.work.personnelinfo.activity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.service.ActivityService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/{id}")
    public ActivityDTO getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    @GetMapping("getByPersonelId/{personelId}")
    public List<ActivityDTO> getActivitiesByPersonelId(@PathVariable Long personelId) {
        return activityService.getActivitiesByPersonelId(personelId);
    }

    @PostMapping("/add")
    public ActivityDTO addActivity(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @ModelAttribute ActivityDTO activityDTO) throws IOException {
        return activityService.addActivity(activityDTO, file);
    }

    @PutMapping("/update/{activityId}")
    public ActivityDTO updateActivity(@PathVariable Long activityId,
                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                      @ModelAttribute ActivityDTO activityDTO) throws IOException {
        return activityService.updateActivity(activityId, activityDTO, file);
    }

    @DeleteMapping("/delete/{activityId}")
    public void deleteActivity(@PathVariable Long activityId) throws FileNotFoundException {
        activityService.deleteActivity(activityId);
    }
}