package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una evaluación")
public class EvaluacionDTO {

    @Schema(description = "ID único de la evaluación",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEvaluacion;

    @Schema(description = "Título de la evaluación",
            example = "Examen final de Java")
    private String titulo;

    @Schema(description = "Descripción de la evaluación",
            example = "Evaluación con preguntas teóricas y prácticas")
    private String descripcion;

    @Schema(description = "Tipo de evaluación",
            example = "MCQ",
            allowableValues = {"MCQ", "Abierta", "Mixta"})
    private String tipo;

    @Schema(description = "Puntaje máximo de la evaluación",
            example = "100.00")
    private BigDecimal puntajeMax;

    @Schema(description = "ID del módulo al que pertenece",
            example = "12")
    private Integer moduloId;

    @Schema(description = "Título del módulo asociado",
            example = "Introducción a Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String moduloTitulo;
}