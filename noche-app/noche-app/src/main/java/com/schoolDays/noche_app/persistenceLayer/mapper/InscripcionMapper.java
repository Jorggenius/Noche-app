package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface InscripcionMapper {

    // --- Entity → DTO ---
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    InscripcionDTO toDTO(InscripcionEntity entity);

    List<InscripcionDTO> toDTOList(List<InscripcionEntity> entities);

    // --- DTO → Entity ---
    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    InscripcionEntity toEntity(InscripcionDTO dto);
}
