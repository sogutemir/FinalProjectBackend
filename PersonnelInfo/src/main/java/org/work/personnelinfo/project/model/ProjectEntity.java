package org.work.personnelinfo.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.work.personnelinfo.personel.model.PersonelEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personel_project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Project name cannot be empty")
    @Column(name = "project_name")
    private String projectName;

    @NotEmpty(message = "Team name cannot be empty")
    @Column(name = "team_name")
    private String teamName;

    @NotEmpty(message = "Project task cannot be empty")
    @Column(name = "project_task")
    private String projectTask;

    @Past(message = "Project start date must be in the past")
    @Column(name = "project_start_date")
    private LocalDate projectStartDate;

    @Column(name = "project_status")
    private Boolean projectStatus;

    @PastOrPresent(message = "Project end date must be today or in the past")
    @Column(name = "project_finish_date")
    private LocalDate projectFinishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personel_id", nullable = false)
    private PersonelEntity personel;
}
