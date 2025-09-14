package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo módulo")
public class ModuloCreateDTO {

    @Schema(description = "ID del curso al que pertenece el módulo",
            example = "3", required = true)
    private Long cursoId;

    @Schema(description = "Título del módulo",
            example = "Introducción a Java",
            required = true, maxLength = 150)
    private String titulo;

    @Schema(description = "Tipo de contenido del módulo",
            example = "Video",
            required = true,
            allowableValues = {"Video", "Texto", "PDF", "Quiz", "Práctica"})
    private String tipo;

    @Schema(description = "Orden del módulo dentro del curso",
            example = "1", required = true)
    private Integer orden;
}