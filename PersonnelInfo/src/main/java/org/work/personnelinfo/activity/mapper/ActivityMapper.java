package org.work.personnelinfo.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.model.ActivityEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    @Mapping(target = "personelId", source = "personel.id")
    ActivityDTO modelToDTO(ActivityEntity activityEntity);

    @Mapping(target = "personel.id", source = "personelId")
    ActivityEntity dtoToModel(ActivityDTO activityDTO);

    @Mapping(target = "personel.id", source = "personelId")
    void updateModel(ActivityDTO activityDTO,@MappingTarget ActivityEntity activityEntity);
}
