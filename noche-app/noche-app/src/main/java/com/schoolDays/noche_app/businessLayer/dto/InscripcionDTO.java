package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una inscripción")
public class InscripcionDTO {

    @Schema(description = "ID único de la inscripción",
            example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario inscrito", example = "12")
    private Long usuarioId;

    @Schema(description = "Nombre del usuario inscrito",
            example = "Carlos Ramírez", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del curso inscrito", example = "5")
    private Long cursoId;

    @Schema(description = "Título del curso inscrito",
            example = "Curso de Spring Boot Avanzado", accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;

    @Schema(description = "Progreso del curso en porcentaje",
            example = "75.50")
    private BigDecimal progreso;

    @Schema(description = "Fecha en que se realizó la inscripción",
            example = "2025-09-11")
    private LocalDate fechaInscripcion;

    @Schema(description = "Estado actual de la inscripción",
            example = "En progreso",
            allowableValues = {"Inscrito", "En progreso", "Completado"})
    private String estado;
}