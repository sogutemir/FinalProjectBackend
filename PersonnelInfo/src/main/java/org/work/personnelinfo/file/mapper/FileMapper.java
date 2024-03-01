package org.work.personnelinfo.file.mapper;


import org.mapstruct.*;
import org.work.personnelinfo.file.dto.FileDTO;
import org.work.personnelinfo.file.model.FileEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FileMapper {

    @Mapping(target = "personelId", source = "personel.id")
    @Mapping(target = "fileName", source = "resourceFile.name")
    @Mapping(target = "fileType", source = "resourceFile.type")
    @Mapping(target = "fileId", source = "resourceFile.id")
    FileDTO modelToDTO(FileEntity fileEntity);

    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "resourceFile", ignore = true)
    FileEntity dtoToModel(FileDTO fileDTO);

    @Mapping(target = "resourceFile", ignore = true)
    @Mapping(target = "personel.id", source = "personelId")
    @Mapping(target = "id", ignore = true)
    void updateModel(FileDTO fileDTO,@MappingTarget FileEntity fileEntity);

}
