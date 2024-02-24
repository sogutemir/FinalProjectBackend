package org.work.personnelinfo.slide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlideDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime uploadDate;

    private Long photoId;
}
