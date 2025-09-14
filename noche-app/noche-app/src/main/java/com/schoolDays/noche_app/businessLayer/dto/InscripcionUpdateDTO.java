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
@Schema(description = "Datos para actualizar una inscripción existente")
public class InscripcionUpdateDTO {

    @Schema(description = "Nuevo progreso del curso en porcentaje",
            example = "85.25", minimum = "0.00")
    private BigDecimal progreso;

    @Schema(description = "Fecha actualizada de inscripción",
            example = "2025-09-15")
    private LocalDate fechaInscripcion;

    @Schema(description = "Nuevo estado de la inscripción",
            example = "Completado",
            allowableValues = {"Inscrito", "En progreso", "Completado"})
    private String estado;
}