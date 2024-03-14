package org.work.personnelinfo.personel.model;

import java.time.LocalDate;

public interface PersonelProjection {
    String getName();
    String getSurname();
    String getIdentityNumber();
    String getAcademicTitle();
    LocalDate getDateOfBirth();
    String getEmail();
    Boolean getIsMale();
    String getPhone();
    String getBloodType();
    String getEmergencyContact();
    String getEmergencyContactPhone();
    String getResidenceAddress();
    LocalDate getStartDateOfEmployment();
    String getWorkingType();
    String getMentor();
    String getRegistrationNo();
    String getDepartment();
    String getCadre();
    String getPosition();
    String getTitle();
    String getTeamName();
}
