package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo curso")
public class CursoCreateDTO {

    @Schema(description = "Título del curso",
            example = "Introducción a Spring Boot",
            required = true, maxLength = 150)
    private String titulo;

    @Schema(description = "Descripción detallada del curso",
            example = "Curso introductorio sobre el framework Spring Boot y sus principales características")
    private String descripcion;

    @Schema(description = "Duración estimada del curso en horas",
            example = "40", required = true)
    private Integer duracionEstim;

    @Schema(description = "Nivel de dificultad del curso",
            example = "Básico", required = true,
            allowableValues = {"Básico", "Intermedio", "Avanzado"})
    private String nivel;
}