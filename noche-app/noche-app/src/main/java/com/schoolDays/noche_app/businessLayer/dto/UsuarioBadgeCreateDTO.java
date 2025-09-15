package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para otorgar un badge a un usuario")
public class UsuarioBadgeCreateDTO {

    @Schema(description = "ID del usuario al que se otorga el badge",
            example = "12",
            required = true)
    private Integer usuarioId;

    @Schema(description = "ID del badge que se otorga",
            example = "3",
            required = true)
    private Integer badgeId;
}
