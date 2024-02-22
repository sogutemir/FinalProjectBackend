package org.work.personnelinfo.file.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.file.dto.FileDTO;
import org.work.personnelinfo.file.model.FileEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    @Mapping(target = "personelId", source = "personel.id")
    FileDTO modelToDTO(FileEntity fileEntity);

    @Mapping(target = "personel.id", source = "personelId")
    FileEntity dtoToModel(FileDTO fileDTO);

    @Mapping(target = "personel.id", source = "personelId")
    void updateModel(FileDTO fileDTO,@MappingTarget FileEntity fileEntity);

}
