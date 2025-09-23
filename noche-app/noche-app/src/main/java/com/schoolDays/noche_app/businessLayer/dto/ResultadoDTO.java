package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del resultado de evaluación")
public class ResultadoDTO {

    @Schema(description = "ID único del resultado",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idResultado;

    @Schema(description = "Puntaje obtenido",
            example = "85.50",
            required = true,
            minimum = "0")
    private BigDecimal puntaje;

    @Schema(description = "Fecha de realización",
            example = "2025-09-16",
            required = true)
    private LocalDate fechaRealizacion;

    @Schema(description = "ID del usuario",
            example = "1",
            required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario",
            example = "Carlos Pérez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID de la evaluación",
            example = "1",
            required = true)
    private Integer idEvaluacion;

    @Schema(description = "Título de la evaluación",
            example = "Examen final de Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String evaluacionTitulo;

    @Schema(description = "Puntaje máximo de la evaluación",
            example = "100.00",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal evaluacionPuntajeMax;

    @Schema(description = "Porcentaje obtenido",
            example = "85.50",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal porcentaje;

    @Schema(description = "Indica si aprobó (ejemplo: >= 60%)",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean aprobo;
}

