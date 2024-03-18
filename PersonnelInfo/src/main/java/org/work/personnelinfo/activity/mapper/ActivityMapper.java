package org.work.personnelinfo.activity.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.model.ActivityEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityMapper {

    @Mapping(target = "personelId", source = "personel.id")
    @Mapping(target = "fileName", source = "resourceFile.name")
    @Mapping(target = "fileId", source = "resourceFile.id")
    ActivityDTO modelToDTO(ActivityEntity activityEntity);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "resourceFile", ignore = true)
    ActivityEntity dtoToModel(ActivityDTO activityDTO);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resourceFile", ignore = true)
    void updateModel(ActivityDTO activityDTO,@MappingTarget ActivityEntity activityEntity);
}
