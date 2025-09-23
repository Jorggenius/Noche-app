package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.persistenceLayer.entity.ResultadoEntity;
import org.mapstruct.*;
import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        imports = {java.math.RoundingMode.class}
)
public interface ResultadoMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "usuarioNombre", expression = "java(entity.getUsuario().getNombre() + \" \" + entity.getUsuario().getApellido())")
    @Mapping(target = "idEvaluacion", source = "evaluacion.idEvaluacion")
    @Mapping(target = "evaluacionTitulo", source = "evaluacion.titulo")
    @Mapping(target = "evaluacionPuntajeMax", source = "evaluacion.puntajeMax")
    @Mapping(target = "porcentaje", expression = "java(calcularPorcentaje(entity.getPuntaje(), entity.getEvaluacion().getPuntajeMax()))")
    @Mapping(target = "aprobo", expression = "java(calcularAprobacion(entity.getPuntaje(), entity.getEvaluacion().getPuntajeMax()))")
    ResultadoDTO toDTO(ResultadoEntity entity);

    List<ResultadoDTO> toDTOList(List<ResultadoEntity> entities);

    @Mapping(target = "idResultado", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "evaluacion.idEvaluacion", source = "idEvaluacion")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ResultadoEntity toEntity(ResultadoDTO dto);

    @Mapping(target = "idResultado", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "evaluacion.idEvaluacion", source = "idEvaluacion")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ResultadoDTO dto, @MappingTarget ResultadoEntity entity);

    default BigDecimal calcularPorcentaje(BigDecimal puntaje, BigDecimal puntajeMax) {
        if (puntajeMax == null || puntajeMax.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return puntaje.multiply(BigDecimal.valueOf(100)).divide(puntajeMax, 2, RoundingMode.HALF_UP);
    }

    default Boolean calcularAprobacion(BigDecimal puntaje, BigDecimal puntajeMax) {
        BigDecimal porcentaje = calcularPorcentaje(puntaje, puntajeMax);
        return porcentaje.compareTo(BigDecimal.valueOf(60)) >= 0;
    }
}
