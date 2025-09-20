package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.EvaluacionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {PreguntaMapper.class}, // 👈 se integran preguntas
        unmappedTargetPolicy = ReportingPolicy.WARN
)

public interface EvaluacionMapper {

    // Entity → DTO
    @Mapping(target = "idModulo", source = "modulo.idModulo")
    @Mapping(target = "moduloTitulo", source = "modulo.titulo")
    EvaluacionDTO toDTO(EvaluacionEntity entity);

    List<EvaluacionDTO> toDTOList(List<EvaluacionEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo.idModulo", source = "idModulo") // 👈 corregido
    @Mapping(target = "preguntas", ignore = true) // las maneja el servicio
    EvaluacionEntity toEntity(EvaluacionDTO dto);

    // Actualización parcial
    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo", ignore = true)    // no se cambia el módulo
    @Mapping(target = "preguntas", ignore = true) // no se pisan las preguntas
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(EvaluacionDTO dto, @MappingTarget EvaluacionEntity entity);
}
