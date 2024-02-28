package org.work.personnelinfo.activity.dto;

import jakarta.validation.constraints.NotNull;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO  {

    private Long id;
    private String activityName;
    private String description;
    private String link;
    private String eventType;
    private LocalDateTime uploadDate;
    private String fileName;

    public ActivityDTO(Long personelId) {
        this.personelId = personelId;
    }

    @NotNull
    private Long personelId;

}
