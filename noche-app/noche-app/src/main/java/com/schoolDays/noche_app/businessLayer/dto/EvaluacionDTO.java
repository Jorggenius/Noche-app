package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO único para evaluaciones
 * Usado para crear, leer y actualizar evaluaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la evaluación")
public class EvaluacionDTO {

    @Schema(description = "ID único de la evaluación",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEvaluacion;

    @Schema(description = "Título de la evaluación",
            example = "Examen final de Java",
            required = true,
            maxLength = 150)
    private String titulo;

    @Schema(description = "Descripción de la evaluación",
            example = "Evaluación sobre conceptos básicos de Java",
            maxLength = 500)
    private String descripcion;

    @Schema(description = "Tipo de evaluación",
            example = "MCQ",
            required = true,
            allowableValues = {"MCQ", "ABIERTA", "MIXTA"})
    private String tipo;

    @Schema(description = "Puntaje máximo",
            example = "100.00",
            required = true,
            minimum = "0.01")
    private BigDecimal puntaje;

    @Schema(description = "ID del módulo",
            example = "1",
            required = true)
    private Integer idModulo;

    @Schema(description = "Título del módulo",
            example = "Conceptos básicos de Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String moduloTitulo;
}
