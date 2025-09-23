package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.PreguntaDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RespuestaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PreguntaMapper {

    @Mapping(target = "idEvaluacion", source = "evaluacion.idEvaluacion")
    @Mapping(target = "evaluacionTitulo", source = "evaluacion.titulo")
    PreguntaDTO toDTO(PreguntaEntity entity);

    List<PreguntaDTO> toDTOList(List<PreguntaEntity> entities);

    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "evaluacion", ignore = true)
    @Mapping(target = "opciones", ignore = true)
    @Mapping(target = "respuestasUsuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PreguntaEntity toEntity(PreguntaDTO dto);

    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "evaluacion", ignore = true)
    @Mapping(target = "opciones", ignore = true)
    @Mapping(target = "respuestasUsuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PreguntaDTO dto, @MappingTarget PreguntaEntity entity);
}