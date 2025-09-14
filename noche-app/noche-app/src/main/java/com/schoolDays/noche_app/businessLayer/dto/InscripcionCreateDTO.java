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
@Schema(description = "Datos para crear una nueva inscripción")
public class InscripcionCreateDTO {

    @Schema(description = "ID del usuario que se inscribe",
            example = "12", required = true)
    private Long usuarioId;

    @Schema(description = "ID del curso al que se inscribe",
            example = "5", required = true)
    private Long cursoId;

    @Schema(description = "Progreso inicial del curso (por defecto 0.00)",
            example = "0.00", required = true, minimum = "0.00")
    private BigDecimal progreso;

    @Schema(description = "Fecha de inscripción",
            example = "2025-09-11", required = true)
    private LocalDate fechaInscripcion;

    @Schema(description = "Estado inicial de la inscripción",
            example = "Inscrito", required = true,
            allowableValues = {"Inscrito", "En progreso", "Completado"})
    private String estado;
}
