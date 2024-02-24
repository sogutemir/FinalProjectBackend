package org.work.personnelinfo.slide.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.slide.dto.SlideDTO;
import org.work.personnelinfo.slide.model.SlideEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SlideMapper {

    @Mapping(target = "photoId", source = "resourceFile.id")
    @Mapping(target = "uploadDate", source = "uploadDate")
    SlideDTO modelToDTO(SlideEntity slideEntity);

    SlideEntity dtoToModel(SlideDTO slideDTO);

    void updateModel(SlideDTO slideDTO,@MappingTarget SlideEntity slideEntity);
}
