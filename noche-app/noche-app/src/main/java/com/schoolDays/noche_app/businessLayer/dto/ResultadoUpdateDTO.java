package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un resultado de evaluación")
public class ResultadoUpdateDTO {

    @Schema(description = "Nuevo puntaje obtenido en la evaluación",
            example = "90.75",
            minimum = "0.00")
    private BigDecimal puntaje;
}
