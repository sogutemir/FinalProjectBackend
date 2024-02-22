package org.work.personnelinfo.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.project.dto.ProjectDTO;
import org.work.personnelinfo.project.model.ProjectEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    @Mapping(target = "personelId", source = "personel.id")
    ProjectDTO modelToDTO(ProjectEntity projectEntity);
    @Mapping(target = "personel.id", source = "personelId")
    ProjectEntity dtoToModel(ProjectDTO projectDTO);

    @Mapping(target = "personel.id", source = "personelId")
    ProjectEntity updateModel(ProjectDTO projectDTO, @MappingTarget ProjectEntity projectEntity);

}
