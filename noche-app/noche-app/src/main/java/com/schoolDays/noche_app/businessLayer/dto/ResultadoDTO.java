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
@Schema(description = "Información completa del resultado de una evaluación")
public class ResultadoDTO {

    @Schema(description = "ID único del resultado",
            example = "201",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idResultado;

    @Schema(description = "ID del usuario que realizó la evaluación",
            example = "12")
    private Integer usuarioId;

    @Schema(description = "Nombre completo del usuario",
            example = "Carlos Ramírez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID de la evaluación realizada",
            example = "5")
    private Integer evaluacionId;

    @Schema(description = "Título de la evaluación",
            example = "Examen Final de Java",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String evaluacionTitulo;

    @Schema(description = "Puntaje obtenido en la evaluación",
            example = "85.50")
    private BigDecimal puntaje;

    @Schema(description = "Fecha en que se realizó la evaluación",
            example = "2025-09-14")
    private LocalDate fechaRealizacion;
}

