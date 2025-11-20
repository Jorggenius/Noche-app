package com.Curso.CapacitacionInterna.dto;

import com.Curso.CapacitacionInterna.model.Inscripcion;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private Long cursoId;
    private String cursoTitulo;
    private String cursoDescripcion;
    private Integer cursoDuracionEstimada;
    private String cursoNivel;
    private String cursoInstructorNombre;
    private Boolean cursoActivo;
    private Integer progreso;
    private LocalDateTime fechaInscripcion;
    private Inscripcion.Estado estado;
}