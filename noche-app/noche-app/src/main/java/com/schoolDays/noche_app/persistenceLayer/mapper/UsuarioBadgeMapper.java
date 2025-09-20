package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.UsuarioBadgeDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioBadgeEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioBadgeMapper {

    // Entity → DTO
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "idBadge", source = "badge.idBadge")
    @Mapping(target = "badgeNombre", source = "badge.nombre")
    UsuarioBadgeDTO toDTO(UsuarioBadgeEntity entity);

    List<UsuarioBadgeDTO> toDTOList(List<UsuarioBadgeEntity> entities);

    // DTO → Entity
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "badge.idBadge", source = "idBadge")
    UsuarioBadgeEntity toEntity(UsuarioBadgeDTO dto);

    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "badge.idBadge", source = "idBadge")
    UsuarioBadgeEntity updateEntityFromDTO(UsuarioBadgeDTO usuarioBadgeDTO, @MappingTarget UsuarioBadgeEntity existingEntity);
}