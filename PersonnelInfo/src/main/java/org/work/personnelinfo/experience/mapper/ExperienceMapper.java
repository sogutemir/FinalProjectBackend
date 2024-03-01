package org.work.personnelinfo.experience.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.experience.dto.ExperienceDTO;
import org.work.personnelinfo.experience.model.ExperienceEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {

    @Mapping(target = "personelId", source = "personel.id")
    ExperienceDTO modelToDTO(ExperienceEntity experienceEntity);


    @Mapping(target = "personel.id", source = "personelId")
    ExperienceEntity dtoToModel(ExperienceDTO experienceDTO);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "id", ignore = true)
    void updateModel(ExperienceDTO experienceDTO,@MappingTarget ExperienceEntity experienceEntity);
}
