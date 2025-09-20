package com.schoolDays.noche_app.businessLayer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO único para certificados
 * Usado para crear, leer y actualizar certificados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del certificado")
public class CertificadoDTO {

    @Schema(description = "ID único del certificado",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCertificado;

    @Schema(description = "Fecha de emisión",
            example = "2025-09-16",
            required = true)
    private LocalDate fechaEmision;

    @Schema(description = "Hash único de verificación",
            example = "a1b2c3d4e5f6...",
            required = true,
            maxLength = 255)
    private String hash;

    @Schema(description = "ID del usuario certificado",
            example = "1",
            required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario certificado",
            example = "Carlos Pérez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del curso certificado",
            example = "1",
            required = true)
    private Integer idCurso;

    @Schema(description = "Título del curso certificado",
            example = "Introducción a Spring Boot",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;
}
