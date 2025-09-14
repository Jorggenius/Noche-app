package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una evaluación existente")
public class EvaluacionUpdateDTO {

    @Schema(description = "Nuevo título de la evaluación",
            example = "Examen Intermedio de Java", maxLength = 150)
    private String titulo;

    @Schema(description = "Nuevo tipo de evaluación",
            example = "Abierta",
            allowableValues = {"MCQ", "Abierta", "Mixta"})
    private String tipo;

    @Schema(description = "Nuevo puntaje máximo de la evaluación",
            example = "50.00", minimum = "0.01")
    private BigDecimal puntajeMax;
}