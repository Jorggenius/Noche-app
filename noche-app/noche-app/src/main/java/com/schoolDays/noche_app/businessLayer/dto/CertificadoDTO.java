package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un certificado")
public class CertificadoDTO {

    @Schema(description = "ID único del certificado",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario al que pertenece el certificado",
            example = "12")
    private Long usuarioId;

    @Schema(description = "Nombre del usuario certificado",
            example = "Carlos Ramírez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del curso que genera el certificado",
            example = "5")
    private Long cursoId;

    @Schema(description = "Título del curso certificado",
            example = "Spring Boot Avanzado",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;

    @Schema(description = "Fecha de emisión del certificado",
            example = "2025-09-11")
    private LocalDate fechaEmision;

    @Schema(description = "Código único de verificación del certificado",
            example = "abc123xyz789")
    private String hash;
}