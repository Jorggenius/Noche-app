package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un rol existente")
public class RolUpdateDTO {

    @Schema(description = "Nuevo nombre del rol",
            example = "INSTRUCTOR_SENIOR",
            maxLength = 50)
    private String nombreRol;

    @Schema(description = "Nueva descripción del rol",
            example = "Rol con permisos avanzados para gestión de cursos y evaluaciones")
    private String descripcion;
}
