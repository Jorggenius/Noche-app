package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva inscripción")
public class InscripcionCreateDTO {

    @Schema(description = "ID del usuario que se inscribe",
            example = "12",
            required = true)
    private Integer usuarioId;

    @Schema(description = "ID del curso al que se inscribe",
            example = "5",
            required = true)
    private Integer cursoId;
}
