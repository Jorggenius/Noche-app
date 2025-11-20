package com.Curso.CapacitacionInterna.dto;

import com.Curso.CapacitacionInterna.model.Curso;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoCreateDTO {
    private String titulo;
    private String descripcion;
    private Integer duracionEstimada;
    private Curso.Nivel nivel;
    private Long instructorId;
    private List<ModuloCreateDTO> modulos;
}