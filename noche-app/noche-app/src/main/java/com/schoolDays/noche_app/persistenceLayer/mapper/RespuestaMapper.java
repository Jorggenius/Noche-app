package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RespuestaMapper  {

    @Mapping(target = "idPregunta", source = "pregunta.idPregunta")
    @Mapping(target = "preguntaEnunciado", source = "pregunta.enunciado")
    RespuestaDTO toDTO(RespuestaEntity entity);

    List<RespuestaDTO> toDTOList(List<RespuestaEntity> entities);

    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RespuestaEntity toEntity(RespuestaDTO dto);

    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RespuestaDTO dto, @MappingTarget RespuestaEntity entity);
}
