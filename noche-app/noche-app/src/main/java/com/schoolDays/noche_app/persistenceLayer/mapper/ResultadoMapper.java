package com.schoolDays.noche_app.persistenceLayer.mapper;
import com.schoolDays.noche_app.businessLayer.dto.ResultadoCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;
import com.schoolDays.noche_app.businessLayer.dto.ResultadoUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultadoMapper {

    // Entity -> DTO
    @Mapping(target = "usuarioId", source = "usuario.idUsuario")
    @Mapping(
            target = "usuarioNombre",
            expression = "java(entity.getUsuario().getNombre() + \" \" + entity.getUsuario().getApellido())")
    @Mapping(target = "evaluacionId", source = "evaluacion.idEvaluacion")
    @Mapping(target = "evaluacionTitulo", source = "evaluacion.titulo")
    ResultadoDTO toDTO(ResultadoEntity entity);

    List<ResultadoDTO> toDTOList(List<ResultadoEntity> entities);

    // CreateDTO -> Entity
    @Mapping(target = "idResultado", ignore = true)
    @Mapping(target = "usuario.idUsuario",     source = "usuarioId")
    @Mapping(target = "evaluacion.idEvaluacion", source = "evaluacionId")
    @Mapping(target = "fechaRealizacion", expression = "java(java.time.LocalDate.now())")
    ResultadoEntity toEntity(ResultadoCreateDTO dto);

    // UpdateDTO -> Entity (solo cambia puntaje)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idResultado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "evaluacion", ignore = true)
    @Mapping(target = "fechaRealizacion", ignore = true)
    void updateEntityFromDTO(ResultadoUpdateDTO dto, @MappingTarget ResultadoEntity entity);

//    // Métodos auxiliares para mapear relaciones por ID
//    @Named("mapUsuario")
//    default UsuarioEntity mapUsuario(Integer idUsuario) {
//        if (idUsuario == null) return null;
//        UsuarioEntity usuario = new UsuarioEntity();
//        usuario.setIdUsuario(idUsuario);
//        return usuario;
//    }
//
//    @Named("mapEvaluacion")
//    default EvaluacionEntity mapEvaluacion(Integer idEvaluacion) {
//        if (idEvaluacion == null) return null;
//        EvaluacionEntity evaluacion = new EvaluacionEntity();
//        evaluacion.setIdEvaluacion(idEvaluacion);
//        return evaluacion;
//    }
}