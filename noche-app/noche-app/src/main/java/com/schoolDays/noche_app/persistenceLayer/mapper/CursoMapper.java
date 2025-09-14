package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface CursoMapper {

    // Entity → DTO
    @Mapping(target = "creadoPorId", source = "creadoPor.idUsuario")
    @Mapping(target = "creadorNombre", source = "creadoPor.nombre")
    @Mapping(target = "creadorApellido", source = "creadoPor.apellido")
    CursoDTO toDTO(CursoEntity entity);

    // Lista de entidades → lista de DTOs
    List<CursoDTO> toDTOList(List<CursoEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor", source = "creadoPorId", qualifiedByName = "createUsuarioEntityFromId")
    CursoEntity toEntity(CursoDTO dto);

    // Actualización parcial
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor", ignore = true) // no se cambia el creador en update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CursoDTO dto, @MappingTarget CursoEntity entity);

    // Método auxiliar: crear UsuarioEntity solo con el id
    @Named("createUsuarioEntityFromId")
    default UsuarioEntity createUsuarioEntityFromId(Integer idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(idUsuario);
        return usuario;
    }
}