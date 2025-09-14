package com.schoolDays.noche_app.persistenceLayer.mapper;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioUpdateDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RolEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {

    // ✅ Entity → DTO (LECTURA)
    @Mapping(target = "rolId", source = "rol.idRol")
    @Mapping(target = "rolNombre", source = "rol.nombreRol")
    UsuarioDTO toDTO(UsuarioEntity entity);

    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);

    // ✅ CreateDTO → Entity (CREAR)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", source = "rolId", qualifiedByName = "createRolEntityFromId")
    UsuarioEntity toEntity(UsuarioCreateDTO createDTO);

    // ✅ UpdateDTO → Actualizar Entity existente (PATCH/PUT)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", ignore = true) // el rol no se actualiza desde este DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioUpdateDTO updateDTO, @MappingTarget UsuarioEntity entity);

    // ✅ DTO → Entity (casos especiales/testing)
    @Mapping(target = "rol", source = "rolId", qualifiedByName = "createRolEntityFromId")
    UsuarioEntity toEntity(UsuarioDTO dto);

    // ✅ Método auxiliar: crea un RolEntity solo con ID
    @Named("createRolEntityFromId")
    default RolEntity createRolEntityFromId(Integer rolId) {
        if (rolId == null) {
            return null;
        }
        RolEntity rol = new RolEntity();
        rol.setIdRol(rolId);
        return rol;
    }
}