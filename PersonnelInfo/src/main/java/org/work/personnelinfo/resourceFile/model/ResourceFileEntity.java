package org.work.personnelinfo.resourceFile.model;

import jakarta.persistence.*;
import lombok.*;
import org.work.personnelinfo.activity.model.ActivityEntity;
import org.work.personnelinfo.file.model.FileEntity;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resource_file")
public class ResourceFileEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    @Column(name="resource_file_Data")
    private byte[] data;


    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }


    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private PersonelEntity personel;

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private FileEntity file;

    @OneToOne(mappedBy = "resourceFile", fetch = FetchType.LAZY)
    private ActivityEntity activity;
}
