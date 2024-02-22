package org.work.personnelinfo.activity.dto;

import jakarta.validation.constraints.NotNull;



import lombok.*;

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

    @NotNull
    private Long personelId;

}
