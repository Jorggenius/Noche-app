package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para preguntas
 * Usado para crear, leer y actualizar preguntas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la pregunta")
public class PreguntaDTO {

    @Schema(description = "ID único de la pregunta",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPregunta;

    @Schema(description = "Enunciado de la pregunta",
            example = "¿Qué es una variable en Java?",
            required = true)
    private String enunciado;

    @Schema(description = "Tipo de pregunta",
            example = "MULTIPLE_CHOICE",
            required = true,
            allowableValues = {"MULTIPLE_CHOICE", "VERDADERO_FALSO", "ABIERTA"})
    private String tipoPregunta;

    @Schema(description = "Orden de la pregunta",
            example = "1",
            minimum = "1")
    private Integer orden;

    @Schema(description = "ID de la evaluación",
            example = "1",
            required = true)
    private Integer idEvaluacion;

    @Schema(description = "Título de la evaluación",
            example = "Examen final de Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String evaluacionTitulo;
}
