package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.CertificadoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CertificadoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {

    // Entity -> DTO
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    CertificadoDTO toDTO(CertificadoEntity entity);

    List<CertificadoDTO> toDTOList(List<CertificadoEntity> entities);

    // DTO -> Entity
    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "fechaEmision", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hash", expression = "java(java.util.UUID.randomUUID().toString())")
    CertificadoEntity toEntity(CertificadoDTO dto);

    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "fechaEmision", ignore = true)
    @Mapping(target = "hash", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    CertificadoEntity updateEntityFromDTO(CertificadoDTO certificadoDTO, @MappingTarget CertificadoEntity existingEntity);
}