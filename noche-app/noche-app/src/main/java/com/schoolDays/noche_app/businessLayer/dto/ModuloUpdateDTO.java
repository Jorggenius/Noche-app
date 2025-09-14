package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un módulo existente")
public class ModuloUpdateDTO {

    @Schema(description = "Nuevo título del módulo",
            example = "Fundamentos de Programación", maxLength = 150)
    private String titulo;

    @Schema(description = "Nuevo tipo de contenido del módulo",
            example = "PDF",
            allowableValues = {"Video", "Texto", "PDF", "Quiz", "Práctica"})
    private String tipo;

    @Schema(description = "Nuevo orden del módulo dentro del curso",
            example = "3")
    private Integer orden;
}