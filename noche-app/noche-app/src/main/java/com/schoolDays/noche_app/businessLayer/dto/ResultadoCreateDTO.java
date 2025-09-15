package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar el resultado de una evaluación")
public class ResultadoCreateDTO {

    @Schema(description = "ID del usuario que realizó la evaluación",
            example = "12",
            required = true)
    private Integer usuarioId;

    @Schema(description = "ID de la evaluación realizada",
            example = "5",
            required = true)
    private Integer evaluacionId;

    @Schema(description = "Puntaje obtenido en la evaluación",
            example = "85.50",
            required = true,
            minimum = "0.00")
    private BigDecimal puntaje;
}
