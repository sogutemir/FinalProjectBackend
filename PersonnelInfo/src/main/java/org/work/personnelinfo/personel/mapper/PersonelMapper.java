package org.work.personnelinfo.personel.mapper;


import org.mapstruct.*;
import org.work.personnelinfo.activity.mapper.ActivityMapper;
import org.work.personnelinfo.education.mapper.EducationMapper;
import org.work.personnelinfo.file.mapper.FileMapper;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.project.mapper.ProjectMapper;


@Mapper(componentModel = "spring",  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonelMapper {

    @Mappings({
            @Mapping(target = "photoId", source = "resourceFile.id"),
            @Mapping(target = "employmentStartDate", source = "startDateOfEmployment"),
    })
    PersonelDTO modelToDTO(PersonelEntity personelEntity);


    PersonelEntity dtoToModel(PersonelDTO personelDTO);

    @Mapping(target = "id", ignore = true)
    void updateModel(PersonelDTO personelDTO, @MappingTarget PersonelEntity personelEntity);
}