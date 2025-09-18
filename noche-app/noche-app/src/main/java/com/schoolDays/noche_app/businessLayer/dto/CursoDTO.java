package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO único para cursos
 * Usado para crear, leer y actualizar cursos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del curso")
public class CursoDTO {

    @Schema(description = "ID único del curso",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCurso;

    @Schema(description = "Título del curso",
            example = "Introducción a Spring Boot",
            required = true,
            maxLength = 150)
    private String titulo;

    @Schema(description = "Descripción del curso",
            example = "Curso introductorio sobre el framework Spring Boot",
            maxLength = 500)
    private String descripcion;

    @Schema(description = "Duración estimada en horas",
            example = "40",
            required = true,
            minimum = "1")
    private Integer duracion;

    @Schema(description = "Nivel del curso",
            example = "BASICO",
            required = true,
            allowableValues = {"BASICO", "INTERMEDIO", "AVANZADO"})
    private String nivel;

    @Schema(description = "Fecha de creación",
            example = "2025-09-16",
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaCreacion;

    @Schema(description = "Versión del curso",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer version;

    @Schema(description = "ID del usuario creador",
            example = "1",
            required = true)
    private Integer creadoPorId;

    @Schema(description = "Nombre del creador",
            example = "María González",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String creadoPorNombre;
}
