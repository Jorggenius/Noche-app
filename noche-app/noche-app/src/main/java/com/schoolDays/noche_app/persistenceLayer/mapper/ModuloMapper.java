package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.ModuloDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)

public interface ModuloMapper {

    // Entity → DTO
    // Entity → DTO
    @Mapping(target = "idCurso", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    @Mapping(target = "tipo", source = "tipo")
    ModuloDTO toDTO(ModuloEntity entity);


    List<ModuloDTO> toDTOList(List<ModuloEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso.idCurso", source = "idCurso")   // 🔥 corregido
    @Mapping(target = "evaluaciones", ignore = true)
    ModuloEntity toEntity(ModuloDTO dto);

    // Actualización parcial
    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "evaluaciones", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ModuloDTO dto, @MappingTarget ModuloEntity entity);
}