package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una pregunta")
public class PreguntaDTO {

    @Schema(description = "ID único de la pregunta",
            example = "7",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPregunta;

    @Schema(description = "Texto del enunciado de la pregunta",
            example = "¿Qué significa JDK en Java?")
    private String enunciado;

    @Schema(description = "ID de la evaluación asociada",
            example = "3")
    private Integer evaluacionId;

    @Schema(description = "Título de la evaluación asociada",
            example = "Evaluación de Fundamentos de Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String evaluacionTitulo;

    @Schema(description = "Listado de opciones de respuesta para la pregunta")
    private List<RespuestaDTO> respuestas;
}