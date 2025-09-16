package com.schoolDays.noche_app.persistenceLayer.mapper;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioCreateDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioUpdateDTO;
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
    @Mapping(target = "rol.idRol", source = "rolId") // 👈 clave: mapeo anidado
    @Mapping(target = "cursosCreados", ignore = true) // o inicializa en @AfterMapping si necesitas vacío
    @Mapping(target = "contrasena", ignore = true)
    UsuarioEntity toEntity(UsuarioCreateDTO createDTO);

    // ✅ UpdateDTO → Actualizar Entity existente (PATCH/PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "rol", ignore = true) // no se actualiza desde este DTO
    @Mapping(target = "cursosCreados", ignore = true) // colecciones
    @Mapping(target = "contrasena", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateDTO updateDTO, @MappingTarget UsuarioEntity entity);

    // ✅ DTO → Entity (casos especiales/testing)
    @Mapping(target = "idUsuario", ignore = true)              // si el DTO no trae el id o no quieres setearlo
    @Mapping(target = "rol.idRol", source = "rolId")           // ← clave: id anidado
    @Mapping(target = "contrasena", ignore = true)             // no viene en DTO, evita unmapped
    @Mapping(target = "cursosCreados", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    // ✅ Método auxiliar: crea un RolEntity solo con ID
   // @Named("createRolEntityFromId")
   // default RolEntity createRolEntityFromId(Integer rolId) {
     //   if (rolId == null) {
       //     return null;
       // }
       // RolEntity rol = new RolEntity();
        //rol.setIdRol(rolId);
        //return rol;
    //}
}