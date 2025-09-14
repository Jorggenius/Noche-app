package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.RolDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RolEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RolMapper {

    // Convierte RolEntity → RolDTO
    RolDTO toDTO(RolEntity entity);

    // Convierte lista de entidades → lista de DTOs
    List<RolDTO> toDTOList(List<RolEntity> entities);

    // Convierte RolDTO → RolEntity (crear)
    @Mapping(target = "usuarios", ignore = true) // para evitar ciclos
    RolEntity toEntity(RolDTO dto);

    // Actualiza RolEntity existente con datos de RolDTO
    @Mapping(target = "usuarios", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RolDTO dto, @MappingTarget RolEntity entity);
}