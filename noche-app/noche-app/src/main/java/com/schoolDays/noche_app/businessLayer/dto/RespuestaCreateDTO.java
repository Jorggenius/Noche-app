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
@Schema(description = "Datos para crear una nueva respuesta a una evaluación")
public class RespuestaCreateDTO {

    @Schema(description = "ID de la evaluación asociada",
            example = "5", required = true)
    private Long evaluacionId;

    @Schema(description = "ID del usuario que responde",
            example = "12", required = true)
    private Long usuarioId;

    @Schema(description = "Puntuación obtenida en la respuesta",
            example = "4.50", required = true, minimum = "0.00")
    private BigDecimal puntuacion;

    @Schema(description = "Fecha en que se registró la respuesta",
            example = "2025-09-11", required = true)
    private LocalDate fecha;
}