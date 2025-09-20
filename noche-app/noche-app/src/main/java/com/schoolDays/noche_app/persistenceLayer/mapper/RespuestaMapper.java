package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
@NoArgsConstructor
@AllArgsConstructor
public interface RespuestaMapper {

    // Entity → DTO
    @Mapping(target = "idPregunta", source = "pregunta.idPregunta")
    RespuestaDTO toDTO(RespuestaEntity entity);

    List<RespuestaDTO> toDTOList(List<RespuestaEntity> entities);

    // DTO → Entity
    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true) // se setea en el servicio
    RespuestaEntity toEntity(RespuestaDTO dto);

    // Actualización parcial
    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RespuestaDTO dto, @MappingTarget RespuestaEntity entity);
}
