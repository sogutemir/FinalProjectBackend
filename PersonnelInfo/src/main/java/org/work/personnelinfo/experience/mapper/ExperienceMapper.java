package org.work.personnelinfo.experience.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.experience.dto.ExperienceDTO;
import org.work.personnelinfo.experience.model.ExperienceEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExperienceMapper {

    @Mapping(target = "personelId", source = "personel.id")
    ExperienceDTO modelToDTO(ExperienceEntity experienceEntity);


    @Mapping(target = "personel.id", source = "personelId")
    ExperienceEntity dtoToModel(ExperienceDTO experienceDTO);

    @Mapping(target = "personel.id", source = "personelId")
    void updateModel(ExperienceDTO experienceDTO,@MappingTarget ExperienceEntity experienceEntity);
}
