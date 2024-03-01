package org.work.personnelinfo.education.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.education.dto.EducationDTO;
import org.work.personnelinfo.education.model.EducationEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationMapper {

    @Mapping(target = "personelId", source = "personel.id")
    @Mapping(target = "universityName", source = "universtySchool")
    EducationDTO modelToDTO(EducationEntity educationEntity);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "universtySchool", source = "universityName")
    EducationEntity dtoToModel(EducationDTO educationDTO);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "universtySchool", source = "universityName")
    @Mapping(target = "id", ignore = true)
    void updateModel(EducationDTO educationDTO, @MappingTarget EducationEntity educationEntity);
}
