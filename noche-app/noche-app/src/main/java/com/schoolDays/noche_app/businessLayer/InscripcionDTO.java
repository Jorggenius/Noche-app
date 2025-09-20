package com.schoolDays.noche_app.businessLayer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO único para inscripciones
 * Usado para crear, leer y actualizar inscripciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la inscripción")
public class InscripcionDTO {

    @Schema(description = "ID único de la inscripción",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInscripcion;

    @Schema(description = "Progreso del curso (0-100)",
            example = "75.5",
            minimum = "0",
            maximum = "100")
    private BigDecimal progreso;

    @Schema(description = "Fecha de inscripción",
            example = "2025-09-16",
            required = true)
    private LocalDate fechaInscripcion;

    @Schema(description = "Estado de la inscripción",
            example = "EN_PROGRESO",
            allowableValues = {"INSCRITO", "EN_PROGRESO", "COMPLETADO", "SUSPENDIDO"})
    private String estado;

    @Schema(description = "ID del usuario inscrito",
            example = "1",
            required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario",
            example = "Carlos Pérez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del curso",
            example = "1",
            required = true)
    private Integer idCurso;

    @Schema(description = "Título del curso",
            example = "Introducción a Spring Boot",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;
}
