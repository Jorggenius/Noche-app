package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioUpdateDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaUsuarioEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RespuestaUsuarioMapper {

    // CreateDTO -> Entity
    @Mapping(target = "idRespuestaUsuario", ignore = true)
    @Mapping(target = "usuario", source = "usuarioId", qualifiedByName = "mapUsuario")
    @Mapping(target = "pregunta", source = "preguntaId", qualifiedByName = "mapPregunta")
    @Mapping(target = "respuesta", source = "respuestaId", qualifiedByName = "mapRespuesta")
    @Mapping(target = "correcta", ignore = true)
    @Mapping(target = "fecha", expression = "java(java.time.LocalDate.now())")
    RespuestaUsuarioEntity toEntity(RespuestaUsuarioCreateDTO dto);

    // UpdateDTO -> Entity (solo cambia respuesta y texto)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "respuesta", source = "respuestaId", qualifiedByName = "mapRespuesta")
    void updateEntityFromDTO(RespuestaUsuarioUpdateDTO dto, @MappingTarget RespuestaUsuarioEntity entity);

    // Métodos auxiliares
    @Named("mapUsuario")
    default UsuarioEntity mapUsuario(Integer idUsuario) {
        if (idUsuario == null) return null;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }

    @Named("mapPregunta")
    default PreguntaEntity mapPregunta(Integer idPregunta) {
        if (idPregunta == null) return null;
        PreguntaEntity pregunta = new PreguntaEntity();
        pregunta.setIdPregunta(idPregunta);
        return pregunta;
    }

    @Named("mapRespuesta")
    default RespuestaEntity mapRespuesta(Integer idRespuesta) {
        if (idRespuesta == null) return null;
        RespuestaEntity respuesta = new RespuestaEntity();
        respuesta.setIdRespuesta(idRespuesta);
        return respuesta;
    }
}