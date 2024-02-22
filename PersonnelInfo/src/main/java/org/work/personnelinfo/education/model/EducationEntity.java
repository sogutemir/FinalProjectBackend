package org.work.personnelinfo.education.model;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personel_education")
public class EducationEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Education type cannot be empty")
    @Column(name = "education_type")
    private String educationType;

    @NotEmpty(message = "University/School name cannot be blank")
    @Column(name = "universty_school")
    private String universtySchool;

    @Past(message = "Education start date should be in the past")
    @Column(name = "education_start_date")
    private LocalDate educationStartDate;

    @PastOrPresent(message = "Education end date should be today or in the past")
    @Column(name = "education_end_date")
    private LocalDate educationEndDate;

    @Column(name = "additional_information")
    private String additionalInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personel_id", nullable = false)
    private PersonelEntity personel;
}
