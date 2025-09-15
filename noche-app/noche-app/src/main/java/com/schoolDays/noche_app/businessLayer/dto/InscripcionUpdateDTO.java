package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una inscripción existente")
public class InscripcionUpdateDTO {

    @Schema(description = "Nuevo progreso del curso en porcentaje",
            example = "85.25",
            minimum = "0.00",
            maximum = "100.00")
    private BigDecimal progreso;

    @Schema(description = "Nuevo estado de la inscripción",
            example = "Completado",
            allowableValues = {"Inscrito", "En_progreso", "Completado"})
    private String estado;
}