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
@Schema(description = "Información de una respuesta de una pregunta")
public class RespuestaDTO {

    @Schema(description = "ID único de la respuesta", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRespuesta;

    @Schema(description = "Contenido de la respuesta", example = "La respuesta correcta es A")
    private String contenido;

    @Schema(description = "Indica si la respuesta es correcta", example = "true")
    private Boolean esCorrecta;

    @Schema(description = "ID de la pregunta asociada", example = "7")
    private Integer preguntaId;
}