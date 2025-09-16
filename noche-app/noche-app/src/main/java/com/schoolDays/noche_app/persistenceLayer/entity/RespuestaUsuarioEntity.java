package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa las respuestas de los usuarios
 */
@Entity
@Table(name = "respuesta_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuestaUsuario;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal puntuacion = BigDecimal.ZERO;

    // Para preguntas MCQ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRespuesta")
    private RespuestaEntity respuestaSeleccionada;

    // Para preguntas abiertas
    @Lob
    private String respuestaTexto;

    @Column
    private Boolean correcta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_idevaluacion", nullable = false)
    private EvaluacionEntity evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_idusuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPregunta", nullable = false)
    private PreguntaEntity pregunta;
}
