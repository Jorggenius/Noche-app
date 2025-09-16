package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;
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
    @Mapping(target = "creadoPor.idUsuario", source = "creadoPorId") // 👈 convierte Integer → UsuarioEntity(id)
    @Mapping(target = "modulos", ignore = true)
    CursoEntity toEntity(CursoDTO dto);

    // Actualización parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modulos", ignore = true)
    void updateEntityFromDTO(CursoDTO dto, @MappingTarget CursoEntity entity);

//    // Método auxiliar: crear UsuarioEntity solo con el id
//    @Named("createUsuarioEntityFromId")
//    default UsuarioEntity createUsuarioEntityFromId(Integer idUsuario) {
//        if (idUsuario == null) {
//            return null;
//        }
//        UsuarioEntity usuario = new UsuarioEntity();
//        usuario.setIdUsuario(idUsuario);
//        return usuario;
//    }
}