package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioBadgeEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioBadgeMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", expression = "java(entity.getUsuario().getNombre() + \" \" + entity.getUsuario().getApellido())")
    @Mapping(target = "idBadge", source = "badge.idBadge")
    @Mapping(target = "badgeNombre", source = "badge.nombre")
    @Mapping(target = "badgeIcono", source = "badge.icono")
    UsuarioBadgeDTO toDTO(UsuarioBadgeEntity entity);

    List<UsuarioBadgeDTO> toDTOList(List<UsuarioBadgeEntity> entities);

    @Mapping(target = "idUsuarioBadge", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "badge.idBadge", source = "idBadge")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UsuarioBadgeEntity toEntity(UsuarioBadgeDTO dto);

    @Mapping(target = "idUsuarioBadge", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "badge.idBadge", source = "idBadge")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UsuarioBadgeDTO dto, @MappingTarget UsuarioBadgeEntity entity);
}