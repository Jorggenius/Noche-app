package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface InscripcionMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    InscripcionDTO toDTO(InscripcionEntity entity);

    List<InscripcionDTO> toDTOList(List<InscripcionEntity> entities);

    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InscripcionEntity toEntity(InscripcionDTO dto);

    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InscripcionEntity updateEntityFromDTO(InscripcionDTO inscripcionDTO, @MappingTarget InscripcionEntity existingEntity);
}