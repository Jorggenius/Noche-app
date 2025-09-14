package com.schoolDays.noche_app.persistenceLayer.mapper;

import com.schoolDays.noche_app.businessLayer.dto.ModuloDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ModuloMapper {

    // Entity → DTO
    @Mapping(target = "cursoId", source = "curso.idCurso")
    @Mapping(target = "cursoTitulo", source = "curso.titulo")
    @Mapping(target = "tipo", source = "tipo") // Enum → String (automático con MapStruct)
    @Mapping(target = "cantidadEvaluaciones", expression = "java(entity.getEvaluaciones() != null ? entity.getEvaluaciones().size() : 0)")
    ModuloDTO toDTO(ModuloEntity entity);

    // Lista → Lista DTO
    List<ModuloDTO> toDTOList(List<ModuloEntity> entities);

    // DTO → Entity (crear)
    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso", source = "cursoId", qualifiedByName = "createCursoEntityFromId")
    @Mapping(target = "evaluaciones", ignore = true) // no se cargan aquí
    ModuloEntity toEntity(ModuloDTO dto);

    // Actualización parcial
    @Mapping(target = "idModulo", ignore = true)
    @Mapping(target = "curso", ignore = true) // no se cambia curso en update
    @Mapping(target = "evaluaciones", ignore = true) // tampoco evaluaciones
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ModuloDTO dto, @MappingTarget ModuloEntity entity);

    // Método auxiliar para referenciar curso solo por ID
    @Named("createCursoEntityFromId")
    default CursoEntity createCursoEntityFromId(Integer idCurso) {
        if (idCurso == null) {
            return null;
        }
        CursoEntity curso = new CursoEntity();
        curso.setIdCurso(idCurso);
        return curso;
    }
}