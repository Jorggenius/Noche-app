package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RespuestaMapper {

    // Entity → DTO
    @Mapping(target = "preguntaId", source = "pregunta.idPregunta")
    RespuestaDTO toDTO(RespuestaEntity entity);

    List<RespuestaDTO> toDTOList(List<RespuestaEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true) // se setea en el servicio
    RespuestaEntity toEntity(RespuestaDTO dto);

    // Actualización parcial
    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RespuestaDTO dto, @MappingTarget RespuestaEntity entity);
}