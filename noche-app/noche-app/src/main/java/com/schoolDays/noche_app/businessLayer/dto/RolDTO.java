package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa del rol de usuario")
public class RolDTO {

    @Schema(description = "ID único del rol",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer  id;

    @Schema(description = "Nombre del rol",
            example = "ADMIN")
    private String nombreRol;

    @Schema(description = "Descripción del rol",
            example = "Rol con permisos de administración")
    private String descripcion;
}