package com.schoolDays.noche_app.persistenceLayer.mapper;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
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
public interface UsuarioMapper {

    // ✅ Entity → DTO (LECTURA)
    @Mapping(target = "idRol", source = "rol.idRol")
    @Mapping(target = "nombreRol", source = "rol.nombreRol")
    UsuarioDTO toDTO(UsuarioEntity entity);

    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);

    // ✅ DTO → Entity (CREAR) - usado para POST
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    @Mapping(target = "rol.nombreRol", ignore = true) // se setea por la relación
    @Mapping(target = "cursosCreados", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "badges", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    // ✅ DTO → Actualizar Entity existente (PATCH/PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    @Mapping(target = "rol.nombreRol", ignore = true) // se setea por la relación
    @Mapping(target = "cursosCreados", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "certificados", ignore = true)
    @Mapping(target = "badges", ignore = true)
    void updateEntityFromDTO(UsuarioDTO dto, @MappingTarget UsuarioEntity entity);
}