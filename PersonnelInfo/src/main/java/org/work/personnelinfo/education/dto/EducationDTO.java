package org.work.personnelinfo.education.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationDTO {

    private Long id;
    private String educationType;
    private String universityName;
    private LocalDate educationStartDate;
    private LocalDate educationEndDate;
    private String additionalInformation;

    @NotNull
    private Long personelId;

}
