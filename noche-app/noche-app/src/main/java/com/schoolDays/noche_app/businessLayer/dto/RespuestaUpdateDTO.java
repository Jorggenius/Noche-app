package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una opción de respuesta existente")
public class RespuestaUpdateDTO {

    @Schema(description = "Nuevo contenido de la opción de respuesta",
            example = "Java Virtual Machine")
    private String contenido;

    @Schema(description = "Nueva indicación si la opción es correcta",
            example = "false")
    private Boolean esCorrecta;
}
