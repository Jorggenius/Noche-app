package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.EvaluacionDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {PreguntaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface EvaluacionMapper {

    @Mapping(target = "idModulo", source = "modulo.idModulo")
    @Mapping(target = "moduloTitulo", source = "modulo.titulo")
    EvaluacionDTO toDTO(EvaluacionEntity entity);

    List<EvaluacionDTO> toDTOList(List<EvaluacionEntity> entities);

    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo.idModulo", source = "idModulo")
    @Mapping(target = "preguntas", ignore = true)
    @Mapping(target = "resultados", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EvaluacionEntity toEntity(EvaluacionDTO dto);

    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo", ignore = true)
    @Mapping(target = "preguntas", ignore = true)
    @Mapping(target = "resultados", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(EvaluacionDTO dto, @MappingTarget EvaluacionEntity entity);
}

