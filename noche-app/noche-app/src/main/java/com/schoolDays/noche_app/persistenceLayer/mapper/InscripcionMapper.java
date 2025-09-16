package com.schoolDays.noche_app.persistenceLayer.mapper;


import com.schoolDays.noche_app.businessLayer.dto.InscripcionCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionUpdateDTO;
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
    @Mapping(target = "usuario.idUsuario", source = "usuarioId")     // 👈 clave
    @Mapping(target = "curso.idCurso",     source = "cursoId")       // 👈 clave
    // Ajusta al tipo real de fechaInscripcion:
    @Mapping(target = "fechaInscripcion", expression = "java(java.time.LocalDate.now())")
    // Ajusta al tipo real de 'progreso':
    // - Si es BigDecimal: usa expression con BigDecimal
    // - Si es Double/Float: puedes usar constant = "0.0"
    @Mapping(target = "progreso", expression = "java(new java.math.BigDecimal(\"0.00\"))")
    @Mapping(target = "estado", constant = "Inscrito")
    InscripcionEntity toEntity(InscripcionCreateDTO dto);

    // --- UpdateDTO → actualizar parcialmente ---
    @Mapping(target = "idInscripcion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "fechaInscripcion", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(InscripcionUpdateDTO dto, @MappingTarget InscripcionEntity entity);

//    // --- Helpers para crear entidades "proxy" con solo el ID ---
//    @Named("createUsuarioEntityFromId")
//    default UsuarioEntity createUsuarioEntityFromId(Integer idUsuario) {
//        if (idUsuario == null) return null;
//        UsuarioEntity usuario = new UsuarioEntity();
//        usuario.setIdUsuario(idUsuario);
//        return usuario;
//    }
//
//    @Named("createCursoEntityFromId")
//    default CursoEntity createCursoEntityFromId(Integer idCurso) {
//        if (idCurso == null) return null;
//        CursoEntity curso = new CursoEntity();
//        curso.setIdCurso(idCurso);
//        return curso;
//    }
}