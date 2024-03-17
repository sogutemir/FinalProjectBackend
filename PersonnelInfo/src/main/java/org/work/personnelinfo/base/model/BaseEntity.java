package org.work.personnelinfo.base.model;

import jakarta.persistence.*;
import lombok.*;
import org.work.personnelinfo.resourceFile.model.ResourceFileEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "resource_file_id")
    private ResourceFileEntity resourceFile;

    public Long getPhotoId() {
        if (resourceFile != null) {
            return resourceFile.getId();
        }
        return null;
    }
}

