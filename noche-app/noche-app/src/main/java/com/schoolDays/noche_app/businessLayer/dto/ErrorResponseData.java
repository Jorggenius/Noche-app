package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar de la API")
public class ErrorResponseData {

    @Schema(description = "Marca de tiempo en la que ocurrió el error",
            example = "2025-09-26T00:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP relacionado con el error",
            example = "400")
    private int status;

    @Schema(description = "Error HTTP estándar",
            example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje detallado sobre el error",
            example = "El campo 'titulo' es obligatorio")
    private String message;
}