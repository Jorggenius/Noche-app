package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.ModuloDTO;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ModuloMapper {

    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    @Mapping(target = "tipo", source = "tipo")
    ModuloDTO toDTO(ModuloEntity entity);

    List<ModuloDTO> toDTOList(List<ModuloEntity> entities);

    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso.idCurso", source = "idCurso")
    @Mapping(target = "evaluaciones", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ModuloEntity toEntity(ModuloDTO dto);

    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "evaluaciones", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ModuloDTO dto, @MappingTarget ModuloEntity entity);
}