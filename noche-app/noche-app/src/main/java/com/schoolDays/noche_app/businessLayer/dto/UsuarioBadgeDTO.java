package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un badge otorgado a un usuario")
public class UsuarioBadgeDTO {

    @Schema(description = "ID único de la asignación",
            example = "401",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuarioBadge;

    @Schema(description = "ID del usuario",
            example = "12")
    private Integer usuarioId;

    @Schema(description = "Nombre completo del usuario",
            example = "Carlos Ramírez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del badge",
            example = "3")
    private Integer badgeId;

    @Schema(description = "Nombre del badge",
            example = "Top Performer",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String badgeNombre;

    @Schema(description = "Icono del badge",
            example = "icons/top_performer.png",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String badgeIcono;

    @Schema(description = "Fecha en que se otorgó el badge",
            example = "2025-09-14")
    private LocalDate fechaOtorgado;
}

