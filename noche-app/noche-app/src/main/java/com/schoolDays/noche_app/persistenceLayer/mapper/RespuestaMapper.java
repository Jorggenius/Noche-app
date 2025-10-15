package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RespuestaMapper  {

    // ✅ Mapea de Entity → DTO
    @Mapping(target = "idPregunta", source = "pregunta.idPregunta")
    @Mapping(target = "preguntaEnunciado", source = "pregunta.enunciado")
    RespuestaDTO toDTO(RespuestaEntity entity);

    List<RespuestaDTO> toDTOList(List<RespuestaEntity> entities);

    // ✅ Mapea de DTO → Entity
    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", expression = "java(mapPregunta(dto.getIdPregunta()))") // ← AQUÍ LA MAGIA
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RespuestaEntity toEntity(RespuestaDTO dto);

    @Mapping(target = "idRespuesta", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RespuestaDTO dto, @MappingTarget RespuestaEntity entity);

    // ⚙️ Este método auxiliar crea una PreguntaEntity solo con su ID
    default PreguntaEntity mapPregunta(Integer idPregunta) {
        if (idPregunta == null) {
            return null;
        }
        PreguntaEntity pregunta = new PreguntaEntity();
        pregunta.setIdPregunta(idPregunta);
        return pregunta;
    }
}
