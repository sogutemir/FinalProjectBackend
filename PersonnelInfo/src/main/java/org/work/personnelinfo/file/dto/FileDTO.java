package org.work.personnelinfo.file.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO  {

    private Long id;
    private String fileName;
    private String fileType;
    private String section;
    private LocalDateTime uploadDate;

    @NotNull
    private Long personelId;

}
