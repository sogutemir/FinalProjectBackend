package org.work.personnelinfo.personel.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.model.PersonelEntity;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonelMapper {

    @Mapping(target = "photoId", source = "resourceFile.id")
    PersonelDTO modelToDTO(PersonelEntity personelEntity);

    PersonelEntity dtoToModel(PersonelDTO personelDTO);
    void updateModel(PersonelDTO personelDTO, @MappingTarget PersonelEntity personelEntity);
}