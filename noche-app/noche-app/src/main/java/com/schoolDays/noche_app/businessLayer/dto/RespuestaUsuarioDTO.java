package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de la respuesta de un usuario")
public class RespuestaUsuarioDTO {

    @Schema(description = "ID único de la respuesta del usuario",
            example = "301",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRespuestaUsuario;

    @Schema(description = "ID del usuario que respondió",
            example = "12")
    private Integer usuarioId;

    @Schema(description = "Nombre del usuario",
            example = "Carlos Ramírez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID de la pregunta respondida",
            example = "7")
    private Integer preguntaId;

    @Schema(description = "Enunciado de la pregunta",
            example = "¿Qué significa JDK en Java?",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String preguntaEnunciado;

    @Schema(description = "ID de la opción seleccionada (para preguntas MCQ)",
            example = "15")
    private Integer respuestaId;

    @Schema(description = "Contenido de la opción seleccionada",
            example = "Java Development Kit",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String respuestaContenido;

    @Schema(description = "Texto de la respuesta (para preguntas abiertas)",
            example = "Java Development Kit es un conjunto de herramientas...")
    private String respuestaTexto;

    @Schema(description = "Indica si la respuesta fue correcta",
            example = "true",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean correcta;

    @Schema(description = "Fecha en que se registró la respuesta",
            example = "2025-09-14")
    private LocalDate fecha;
}
