package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del módulo")
public class ModuloDTO {

    @Schema(description = "ID único del módulo", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idModulo;

    @Schema(description = "Título del módulo", example = "Introducción a Java")
    private String titulo;

    @Schema(description = "Descripción del módulo", example = "Conceptos básicos de Java")
    private String descripcion;

    @Schema(description = "Tipo del módulo", example = "Video",
            allowableValues = {"Video", "Texto", "PDF", "Quiz", "Practica"})
    private String tipo;

    @Schema(description = "Orden en el curso", example = "1")
    private Integer orden;

    @Schema(description = "Versión del módulo", example = "1")
    private Integer version;

    @Schema(description = "ID del curso asociado", example = "3")
    private Integer cursoId;

    @Schema(description = "Título del curso asociado", example = "Curso de Programación en Java", accessMode = Schema.AccessMode.READ_ONLY)
    private String cursoTitulo;

    @Schema(description = "Cantidad de evaluaciones del módulo", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cantidadEvaluaciones;
}