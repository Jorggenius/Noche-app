package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.RespuestaUsuarioDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaUsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RespuestaUsuarioMapper {

    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "idPregunta", source = "pregunta.idPregunta")
    @Mapping(target = "idEvaluacion", source = "evaluacion.idEvaluacion")
    @Mapping(target = "idRespuestaSeleccionada", source = "respuestaSeleccionada.idRespuesta")
    @Mapping(target = "preguntaEnunciado", source = "pregunta.enunciado")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    RespuestaUsuarioDTO toDTO(RespuestaUsuarioEntity entity);

    List<RespuestaUsuarioDTO> toDTOList(List<RespuestaUsuarioEntity> entities);

    // DTO → Entity
    @Mapping(target = "idRespuestaUsuario", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "pregunta.idPregunta", source = "idPregunta")
    @Mapping(target = "evaluacion.idEvaluacion", source = "idEvaluacion")
    @Mapping(target = "respuestaSeleccionada.idRespuesta", source = "idRespuestaSeleccionada")
    RespuestaUsuarioEntity toEntity(RespuestaUsuarioDTO dto);

    @Mapping(target = "idRespuestaUsuario", ignore = true)
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "pregunta.idPregunta", source = "idPregunta")
    @Mapping(target = "evaluacion.idEvaluacion", source = "idEvaluacion")
    @Mapping(target = "respuestaSeleccionada.idRespuesta", source = "idRespuestaSeleccionada")
    RespuestaUsuarioEntity updateEntityFromDTO(RespuestaUsuarioDTO respuestaUsuarioDTO, @MappingTarget RespuestaUsuarioEntity existingEntity);
}