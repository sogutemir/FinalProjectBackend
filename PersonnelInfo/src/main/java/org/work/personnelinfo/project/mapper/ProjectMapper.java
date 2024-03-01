package org.work.personnelinfo.project.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.project.dto.ProjectDTO;
import org.work.personnelinfo.project.model.ProjectEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {
    @Mapping(target = "personelId", source = "personel.id")
    ProjectDTO modelToDTO(ProjectEntity projectEntity);
    @Mapping(target = "personel.id", source = "personelId")
    ProjectEntity dtoToModel(ProjectDTO projectDTO);

    @Mapping(target = "personel.id", source = "personelId")
    ProjectEntity updateModel(ProjectDTO projectDTO, @MappingTarget ProjectEntity projectEntity);

}
