package org.work.personnelinfo.slide.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.slide.dto.SlideDTO;
import org.work.personnelinfo.slide.model.SlideEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SlideMapper {

    @Mapping(target = "photoId", source = "resourceFile.id")
    @Mapping(target = "uploadDate", source = "uploadDate")
    SlideDTO modelToDTO(SlideEntity slideEntity);

    @Mapping(target = "resourceFile", ignore = true)
    SlideEntity dtoToModel(SlideDTO slideDTO);

    @Mapping(target = "resourceFile", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateModel(SlideDTO slideDTO,@MappingTarget SlideEntity slideEntity);
}
