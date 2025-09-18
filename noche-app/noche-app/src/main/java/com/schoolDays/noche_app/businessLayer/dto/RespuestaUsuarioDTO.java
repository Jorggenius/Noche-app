package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO único para respuestas de usuario
 * Usado para crear, leer y actualizar respuestas de usuarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la respuesta del usuario")
public class RespuestaUsuarioDTO {

    @Schema(description = "ID único de la respuesta del usuario",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRespuestaUsuario;

    @Schema(description = "Fecha de la respuesta",
            example = "2025-09-16")
    private LocalDate fecha;

    @Schema(description = "Puntuación obtenida",
            example = "10.00",
            minimum = "0")
    private BigDecimal puntuacion;

    @Schema(description = "ID de la opción seleccionada (para MCQ)",
            example = "1")
    private Integer idRespuestaSeleccionada;

    @Schema(description = "Texto de respuesta (para preguntas abiertas)",
            example = "Una variable es un espacio en memoria...")
    private String respuestaTexto;

    @Schema(description = "Indica si la respuesta fue correcta",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean correcta;

    @Schema(description = "ID de la evaluación",
            example = "1",
            required = true)
    private Integer idEvaluacion;

    @Schema(description = "ID del usuario",
            example = "1",
            required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario",
            example = "Carlos Pérez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID de la pregunta",
            example = "1",
            required = true)
    private Integer idPregunta;

    @Schema(description = "Enunciado de la pregunta",
            example = "¿Qué es una variable en Java?",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String preguntaEnunciado;
}
