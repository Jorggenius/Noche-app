package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.RolEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.RolDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RolMapper {

    RolDTO toDTO(RolEntity entity);

    List<RolDTO> toDTOList(List<RolEntity> entities);

    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RolEntity toEntity(RolDTO dto);

    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RolDTO dto, @MappingTarget RolEntity entity);
}