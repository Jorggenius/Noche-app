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
@Schema(description = "Datos para actualizar una respuesta existente")
public class RespuestaUpdateDTO {

    @Schema(description = "Puntuación obtenida en la respuesta",
            example = "3.75", minimum = "0.00")
    private BigDecimal puntuacion;

    @Schema(description = "Fecha actualizada de la respuesta",
            example = "2025-09-12")
    private LocalDate fecha;
}