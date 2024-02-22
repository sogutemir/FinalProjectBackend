package org.work.personnelinfo.education.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.education.dto.EducationDTO;
import org.work.personnelinfo.education.model.EducationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EducationMapper {

    @Mapping(target = "personelId", source = "personel.id")
    @Mapping(target = "universityName", source = "universtySchool")
    EducationDTO modelToDTO(EducationEntity educationEntity);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "universtySchool", source = "universityName")
    EducationEntity dtoToModel(EducationDTO educationDTO);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "universtySchool", source = "universityName")
    void updateModel(EducationDTO educationDTO, @MappingTarget EducationEntity educationEntity);
}
