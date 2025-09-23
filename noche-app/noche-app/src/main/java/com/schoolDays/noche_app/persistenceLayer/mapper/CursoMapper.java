package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface CursoMapper {

    @Mapping(target = "creadoPorId", source = "creadoPor.idUsuario")
    @Mapping(target = "creadoPorNombre", source = "creadoPor.nombre")
    CursoDTO toDTO(CursoEntity entity);

    List<CursoDTO> toDTOList(List<CursoEntity> entities);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor.idUsuario", source = "creadoPorId")
    @Mapping(target = "modulos", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CursoEntity toEntity(CursoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modulos", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CursoDTO dto, @MappingTarget CursoEntity entity);
}