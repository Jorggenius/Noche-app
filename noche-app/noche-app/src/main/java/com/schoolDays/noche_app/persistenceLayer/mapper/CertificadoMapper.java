package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.CertificadoEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.CertificadoDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    CertificadoDTO toDTO(CertificadoEntity entity);

    List<CertificadoDTO> toDTOList(List<CertificadoEntity> entities);

    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "fechaEmision", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hash", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CertificadoEntity toEntity(CertificadoDTO dto);

    @Mapping(target = "idCertificado", ignore = true)
    @Mapping(target = "fechaEmision", ignore = true)
    @Mapping(target = "hash", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CertificadoDTO certificadoDTO, @MappingTarget CertificadoEntity existingEntity);
}