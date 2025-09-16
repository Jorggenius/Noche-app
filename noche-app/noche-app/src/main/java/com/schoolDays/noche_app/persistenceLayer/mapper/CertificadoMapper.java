package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.CertificadoCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.CertificadoDTO;
import com.schoolDays.noche_app.businessLayer.dto.CertificadoUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {

    // Entity -> DTO
    @Mapping(target = "usuarioId", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombreCompleto")
    @Mapping(target = "cursoId", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    CertificadoDTO toDTO(CertificadoEntity entity);

    List<CertificadoDTO> toDTOList(List<CertificadoEntity> entities);

    // CreateDTO -> Entity
    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "usuarioId") // 👈 clave
    @Mapping(target = "curso.idCurso",   source = "cursoId")     // 👈 clave
    @Mapping(target = "fechaEmision", expression = "java(java.time.LocalDate.now())") // ajusta tipo si no es LocalDate
    @Mapping(target = "hash", expression = "java(java.util.UUID.randomUUID().toString())")
    CertificadoEntity toEntity(CertificadoCreateDTO dto);

    // UpdateDTO -> Entity (solo cambia fechaEmision y hash si no son nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "curso", ignore = true)
    void updateEntityFromDTO(CertificadoUpdateDTO dto, @MappingTarget CertificadoEntity entity);

//    // --- Métodos auxiliares para relaciones ---
//    @Named("mapUsuario")
//    default UsuarioEntity mapUsuario(Integer idUsuario) {
//        if (idUsuario == null) return null;
//        UsuarioEntity usuario = new UsuarioEntity();
//        usuario.setIdUsuario(idUsuario);
//        return usuario;
//    }
//
//    @Named("mapCurso")
//    default CursoEntity mapCurso(Integer idCurso) {
//        if (idCurso == null) return null;
//        CursoEntity curso = new CursoEntity();
//        curso.setIdCurso(idCurso);
//        return curso;
//    }
}