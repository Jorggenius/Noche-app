package com.Curso.CapacitacionInterna.dto;

import com.Curso.CapacitacionInterna.model.Curso;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer duracionEstimada;
    private Curso.Nivel nivel;
    private Long instructorId;
    private String instructorNombre;
    private Boolean activo;
}