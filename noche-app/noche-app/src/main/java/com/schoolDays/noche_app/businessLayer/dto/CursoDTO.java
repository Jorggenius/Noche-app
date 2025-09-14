package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un curso")
public class CursoDTO {

    @Schema(description = "ID único del curso", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCurso;

    @Schema(description = "Título del curso", example = "Curso de Programación en Java")
    private String titulo;

    @Schema(description = "Descripción del curso", example = "Aprende Java desde cero con ejercicios prácticos")
    private String descripcion;

    @Schema(description = "Nivel de dificultad", example = "Intermedio")
    private String nivel;

    @Schema(description = "Duración estimada en horas", example = "40")
    private Integer duracionEstimada;

    @Schema(description = "Fecha de creación", example = "2025-09-13", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaCreacion;

    @Schema(description = "Versión del curso", example = "2")
    private Integer version;

    @Schema(description = "ID del usuario que creó el curso", example = "12", required = true)
    private Integer creadoPorId;

    @Schema(description = "Nombre del creador del curso", example = "María", accessMode = Schema.AccessMode.READ_ONLY)
    private String creadorNombre;

    @Schema(description = "Apellido del creador del curso", example = "González", accessMode = Schema.AccessMode.READ_ONLY)
    private String creadorApellido;
}