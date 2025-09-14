package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.EvaluacionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {PreguntaMapper.class}, // 👈 Se integran preguntas (y respuestas en cascada)
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface EvaluacionMapper {

    // Entity → DTO
    @Mapping(target = "moduloId", source = "modulo.idModulo")
    @Mapping(target = "moduloTitulo", source = "modulo.titulo")
    EvaluacionDTO toDTO(EvaluacionEntity entity);

    List<EvaluacionDTO> toDTOList(List<EvaluacionEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo", source = "moduloId", qualifiedByName = "createModuloEntityFromId")
    @Mapping(target = "preguntas", ignore = true) // las maneja el servicio
    EvaluacionEntity toEntity(EvaluacionDTO dto);

    // Actualización parcial
    @Mapping(target = "idEvaluacion", ignore = true)
    @Mapping(target = "modulo", ignore = true)    // no se cambia el módulo
    @Mapping(target = "preguntas", ignore = true) // no se pisan las preguntas
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(EvaluacionDTO dto, @MappingTarget EvaluacionEntity entity);

    // Auxiliar: crear un ModuloEntity solo con ID
    @Named("createModuloEntityFromId")
    default ModuloEntity createModuloEntityFromId(Integer idModulo) {
        if (idModulo == null) {
            return null;
        }
        ModuloEntity modulo = new ModuloEntity();
        modulo.setIdModulo(idModulo);
        return modulo;
    }
}