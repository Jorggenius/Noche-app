package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeUpdateDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioBadgeEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioBadgeMapper {

    // Entity -> DTO
    @Mapping(target = "usuarioId", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombreCompleto")
    @Mapping(target = "badgeId", source = "badge.idBadge")
    @Mapping(target = "badgeNombre", source = "badge.nombre")
    @Mapping(target = "badgeIcono", source = "badge.icono")
    UsuarioBadgeDTO toDTO(UsuarioBadgeEntity entity);

    List<UsuarioBadgeDTO> toDTOList(List<UsuarioBadgeEntity> entities);

    // CreateDTO -> Entity
    @Mapping(target = "idUsuarioBadge", ignore = true)
    @Mapping(target = "usuario", source = "usuarioId", qualifiedByName = "mapUsuario")
    @Mapping(target = "badge", source = "badgeId", qualifiedByName = "mapBadge")
    @Mapping(target = "fechaOtorgado", expression = "java(java.time.LocalDate.now())")
    UsuarioBadgeEntity toEntity(UsuarioBadgeCreateDTO dto);

    // UpdateDTO -> Entity (solo actualiza fecha)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioBadgeUpdateDTO dto, @MappingTarget UsuarioBadgeEntity entity);

    // Métodos auxiliares para relaciones
    @Named("mapUsuario")
    default UsuarioEntity mapUsuario(Integer idUsuario) {
        if (idUsuario == null) return null;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }

    @Named("mapBadge")
    default BadgeEntity mapBadge(Integer idBadge) {
        if (idBadge == null) return null;
        BadgeEntity badge = new BadgeEntity();
        badge.setIdBadge(idBadge);
        return badge;
    }
}