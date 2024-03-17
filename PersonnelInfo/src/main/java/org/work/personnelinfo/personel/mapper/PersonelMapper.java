package org.work.personnelinfo.personel.mapper;


import org.mapstruct.*;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.model.PersonelEntity;


@Mapper(componentModel = "spring",  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonelMapper {

    @Mappings({
            @Mapping(target = "photoId", source = "resourceFile.id"),
    })
    PersonelDTO modelToDTO(PersonelEntity personelEntity);


    PersonelEntity dtoToModel(PersonelDTO personelDTO);

    @Mapping(target = "id", ignore = true)
    void updateModel(PersonelDTO personelDTO, @MappingTarget PersonelEntity personelEntity);
}