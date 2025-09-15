package com.schoolDays.noche_app.persistenceLayer.mapper;


import com.schoolDays.noche_app.businessLayer.dto.InscripcionCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionUpdateDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface InscripcionMapper {

    // --- Entity → DTO ---
    @Mapping(target = "usuarioId", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "cursoId", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    @Mapping(target = "estado", source = "estado") // Enum → String automático
    InscripcionDTO toDTO(InscripcionEntity entity);

    List<InscripcionDTO> toDTOList(List<InscripcionEntity> entities);

    // --- CreateDTO → Entity ---
    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario", source = "usuarioId", qualifiedByName = "createUsuarioEntityFromId")
    @Mapping(target = "curso", source = "cursoId", qualifiedByName = "createCursoEntityFromId")
    @Mapping(target = "fechaInscripcion", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "progreso", constant = "0.00") // inicia en 0
    @Mapping(target = "estado", constant = "Inscrito")
    InscripcionEntity toEntity(InscripcionCreateDTO dto);

    // --- UpdateDTO → actualizar parcialmente ---
    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "fechaInscripcion", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(InscripcionUpdateDTO dto, @MappingTarget InscripcionEntity entity);

    // --- Helpers para crear entidades "proxy" con solo el ID ---
    @Named("createUsuarioEntityFromId")
    default UsuarioEntity createUsuarioEntityFromId(Integer idUsuario) {
        if (idUsuario == null) return null;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }

    @Named("createCursoEntityFromId")
    default CursoEntity createCursoEntityFromId(Integer idCurso) {
        if (idCurso == null) return null;
        CursoEntity curso = new CursoEntity();
        curso.setIdCurso(idCurso);
        return curso;
    }
}