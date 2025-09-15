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
            example = "3",
            required = true)
    private Integer cursoId;

    @Schema(description = "Título del módulo",
            example = "Introducción a Java",
            required = true,
            maxLength = 150)
    private String titulo;

    @Schema(description = "Descripción del módulo",
            example = "Conceptos básicos de programación en Java")
    private String descripcion;

    @Schema(description = "Tipo de contenido del módulo",
            example = "Video",
            required = true,
            allowableValues = {"Video", "Texto", "PDF", "Quiz", "Practica"})
    private String tipo;

    @Schema(description = "Orden del módulo dentro del curso",
            example = "1",
            required = true,
            minimum = "1")
    private Integer orden;
}