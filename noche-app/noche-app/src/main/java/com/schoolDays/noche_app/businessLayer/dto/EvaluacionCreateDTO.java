package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva evaluación")
public class EvaluacionCreateDTO {

    @Schema(description = "ID del módulo al que pertenece la evaluación",
            example = "7", required = true)
    private Long moduloId;

    @Schema(description = "Título de la evaluación",
            example = "Examen Final de Java",
            required = true, maxLength = 150)
    private String titulo;

    @Schema(description = "Tipo de evaluación",
            example = "MCQ",
            required = true,
            allowableValues = {"MCQ", "Abierta", "Mixta"})
    private String tipo;

    @Schema(description = "Puntaje máximo posible en la evaluación",
            example = "100.00", required = true, minimum = "0.01")
    private BigDecimal puntajeMax;
}
