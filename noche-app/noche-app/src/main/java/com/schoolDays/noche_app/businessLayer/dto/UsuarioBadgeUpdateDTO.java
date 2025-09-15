package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una asignación de badge (uso excepcional)")
public class UsuarioBadgeUpdateDTO {

    @Schema(description = "Nueva fecha de otorgamiento del badge",
            example = "2025-09-15")
    private LocalDate fechaOtorgado;
}
