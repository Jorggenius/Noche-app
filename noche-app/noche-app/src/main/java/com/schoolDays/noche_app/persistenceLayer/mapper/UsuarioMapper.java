package com.schoolDays.noche_app.persistenceLayer.mapper;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {

    @Mapping(target = "idRol", source = "rol.idRol")
    @Mapping(target = "nombreRol", source = "rol.nombreRol")
    UsuarioDTO toDTO(UsuarioEntity entity);

    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    @Mapping(target = "rol.nombreRol", ignore = true)
    @Mapping(target = "cursosCreados", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "badges", ignore = true)
    @Mapping(target = "resultados", ignore = true)
    @Mapping(target = "respuestasUsuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    @Mapping(target = "rol.nombreRol", ignore = true)
    @Mapping(target = "cursosCreados", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "badges", ignore = true)
    @Mapping(target = "resultados", ignore = true)
    @Mapping(target = "respuestasUsuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UsuarioDTO dto, @MappingTarget UsuarioEntity entity);
}