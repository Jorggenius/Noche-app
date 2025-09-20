package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
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
public interface CursoMapper {


    // Entity → DTO
    @Mapping(target = "creadoPorId", source = "creadoPor.idUsuario")
    @Mapping(target = "creadoPorNombre", source = "creadoPor.nombre") // 👈 usa solo lo que existe en el DTO
    CursoDTO toDTO(CursoEntity entity);

    // Lista de entidades → lista de DTOs
    List<CursoDTO> toDTOList(List<CursoEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor.idUsuario", source = "creadoPorId")
    @Mapping(target = "modulos", ignore = true)
    CursoEntity toEntity(CursoDTO dto);

    // Actualización parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modulos", ignore = true)
    void updateEntityFromDTO(CursoDTO dto, @MappingTarget CursoEntity entity);
}