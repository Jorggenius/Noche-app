package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo rol")
public class RolCreateDTO {

    @Schema(description = "Nombre del rol",
            example = "INSTRUCTOR",
            required = true,
            maxLength = 50)
    private String nombreRol;

    @Schema(description = "Descripción del rol",
            example = "Rol con permisos para crear y gestionar cursos")
    private String descripcion;
}

