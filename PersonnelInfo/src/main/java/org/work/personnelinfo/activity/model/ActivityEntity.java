package org.work.personnelinfo.activity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.work.personnelinfo.base.model.BaseEntity;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personel_activity")
public class ActivityEntity extends BaseEntity {

    @NotBlank(message = "Activity name cannot be left blank")
    private String activityName;

    @Size(max = 50, message = "Event type must be maximum 50 characters")
    @Column(name = "event_type")
    private String eventType;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

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
