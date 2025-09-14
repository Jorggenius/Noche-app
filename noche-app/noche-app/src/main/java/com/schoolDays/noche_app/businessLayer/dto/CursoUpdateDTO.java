package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un curso existente")
public class CursoUpdateDTO {

    @Schema(description = "Nuevo título del curso",
            example = "Spring Boot Avanzado", maxLength = 150)
    private String titulo;

    @Schema(description = "Nueva descripción del curso",
            example = "Curso avanzado sobre microservicios con Spring Boot")
    private String descripcion;

    @Schema(description = "Nueva duración estimada del curso en horas",
            example = "60")
    private Integer duracionEstim;

    @Schema(description = "Nuevo nivel de dificultad del curso",
            example = "Avanzado",
            allowableValues = {"Básico", "Intermedio", "Avanzado"})
    private String nivel;
}