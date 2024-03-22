package org.work.personnelinfo.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;
    private String projectName;
    private String teamName;
    private String projectTask;
    private LocalDate projectStartDate;
    private LocalDate projectFinishDate;
    private Boolean projectStatus;

    @NotNull
    private Long personelId;
}
