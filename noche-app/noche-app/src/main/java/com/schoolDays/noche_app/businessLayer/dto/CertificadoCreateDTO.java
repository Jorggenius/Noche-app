package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo certificado")
public class CertificadoCreateDTO {

    @Schema(description = "ID del usuario al que pertenece el certificado",
            example = "12",
            required = true)
    private Integer usuarioId;

    @Schema(description = "ID del curso que genera el certificado",
            example = "5",
            required = true)
    private Integer cursoId;
}