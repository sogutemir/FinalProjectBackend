package org.work.personnelinfo.personel.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonelDTO  {

    private Long id;
    private String name;
    private String surname;
    private String identityNumber;
    private String academicTitle;
    private LocalDate dateOfBirth;
    private Boolean isMale;
    private String email;
    private String phone;
    private String emergencyContact;
    private String emergencyContactPhone;
    private String residenceAddress;
    private String registrationNo;
    private String position;
    private String bloodType;
    private String title;
    private String teamName;
    private String department;
    private String workingType;
    private String task;
    private String cadre;
    private String personnelType;
    private String workStatus;
    private Boolean isServiceUsage;
    private String mentor;
    private String projectInProgress;
    private String vehiclePlate;
    private String internalNumber;
    private String roomNumber;
    private Long photoId;
    private LocalDate startDateOfEmployment;

}
