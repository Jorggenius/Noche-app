package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar la respuesta de un usuario")
public class RespuestaUsuarioUpdateDTO {

    @Schema(description = "Nueva opción seleccionada (para preguntas MCQ)",
            example = "16")
    private Integer respuestaId;

    @Schema(description = "Nuevo texto de respuesta (para preguntas abiertas)",
            example = "Java Development Kit es el conjunto completo de herramientas...")
    private String respuestaTexto;
}
