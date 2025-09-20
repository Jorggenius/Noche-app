package com.schoolDays.noche_app.businessLayer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para módulos
 * Usado para crear, leer y actualizar módulos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del módulo")
public class ModuloDTO {

    @Schema(description = "ID único del módulo",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idModulo;

    @Schema(description = "Título del módulo",
            example = "Conceptos básicos de Java",
            required = true,
            maxLength = 150)
    private String titulo;

    @Schema(description = "Descripción del módulo",
            example = "Introducción a los conceptos fundamentales de Java",
            maxLength = 500)
    private String descripcion;

    @Schema(description = "Tipo de contenido",
            example = "VIDEO",
            required = true,
            allowableValues = {"VIDEO", "TEXTO", "PDF", "QUIZ", "PRACTICA"})
    private String tipo;

    @Schema(description = "Orden dentro del curso",
            example = "1",
            required = true,
            minimum = "1")
    private Integer orden;

    @Schema(description = "Versión del módulo",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer version;

    @Schema(description = "ID del curso al que pertenece",
            example = "1",
            required = true)
    private Integer idCurso;

    @Schema(description = "Título del curso",
            example = "Introducción a Spring Boot",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;
}

