package org.work.personnelinfo.file.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.work.personnelinfo.base.model.BaseEntity;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personel_file")
public class FileEntity extends BaseEntity {

    @NotBlank(message = "File type cannot be empty")
    @Column(name = "file_type")
    private String fileType;

    @NotBlank(message = "File name cannot be empty")
    @Column(name = "file_name")
    private String fileName;

    @NotBlank(message = "Section cannot be empty")
    @Column(name = "section")
    private String section;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personel_id", nullable = false)
    private PersonelEntity personel;
}
