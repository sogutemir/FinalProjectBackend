package org.work.personnelinfo.slide.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.work.personnelinfo.base.model.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slide")
public class SlideEntity extends BaseEntity {

    @NotBlank(message = "Title cannot be left blank")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Description cannot be left blank")
    @Column(name = "description")
    private String description;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}
