package org.work.personnelinfo.admin.mapper;

import org.mapstruct.*;
import org.work.personnelinfo.admin.dto.UserDTO;
import org.work.personnelinfo.admin.model.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {
    UserEntity toEntity(UserDTO userDTO);

    UserDTO toDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialUpdate(UserDTO userDTO, @MappingTarget UserEntity userEntity);

}