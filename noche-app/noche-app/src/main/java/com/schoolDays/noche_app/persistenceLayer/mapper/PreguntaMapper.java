package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.PreguntaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RespuestaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.WARN
)
@NoArgsConstructor
@AllArgsConstructor
public interface PreguntaMapper {

    // Entity → DTO
    @Mapping(target = "idEvaluacion", source = "evaluacion.idEvaluacion")
    @Mapping(target = "evaluacionTitulo", source = "evaluacion.titulo")
    PreguntaDTO toDTO(PreguntaEntity entity);


    List<PreguntaDTO> toDTOList(List<PreguntaEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "evaluacion", ignore = true) // se setea en el servicio
    PreguntaEntity toEntity(PreguntaDTO dto);

    // Actualización parcial
    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "evaluacion", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PreguntaDTO dto, @MappingTarget PreguntaEntity entity);
}