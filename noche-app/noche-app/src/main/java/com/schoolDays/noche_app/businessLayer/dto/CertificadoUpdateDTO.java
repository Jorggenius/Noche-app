package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un certificado existente")
public class CertificadoUpdateDTO {

    @Schema(description = "Nueva fecha de emisión del certificado",
            example = "2025-09-15")
    private LocalDate fechaEmision;

    @Schema(description = "Nuevo código único de verificación del certificado",
            example = "xyz789abc456", maxLength = 255)
    private String hash;
}