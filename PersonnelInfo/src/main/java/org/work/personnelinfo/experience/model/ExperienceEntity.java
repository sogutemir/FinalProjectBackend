package org.work.personnelinfo.experience.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personel_experience")
public class ExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Institution name cannot be blank")
    @Column(name = "institution_name")
    private String institutionName;

    @NotEmpty(message = "Working Format Cannot Be Empty")
    @Column(name = "work_type")
    private String workType;

    @Past(message = "Job start date must be in the past")
    @Column(name = "job_start_date")
    private LocalDate jobStartDate;

    @PastOrPresent(message = "Job due date must be today or in the past")
    @Column(name = "job_end_date")
    private LocalDate jobEndDate;

    @Column(name = "work_position")
    private String workPosition;

    @Column(name = "work_description")
    private String workDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personel_id", nullable = false)
    private PersonelEntity personel;

}
