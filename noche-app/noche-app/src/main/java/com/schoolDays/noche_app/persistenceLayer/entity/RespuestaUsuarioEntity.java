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
@Table(name = "RespuestaUsuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaUsuarioEntity extends BaseEntity { // ✅ AGREGAR extends BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuestaUsuario;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal puntuacion = BigDecimal.ZERO;

    // Para preguntas MCQ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRespuesta") // ✅ CORREGIDO
    private RespuestaEntity respuestaSeleccionada;

    // Para preguntas abiertas
    @Column(columnDefinition = "TEXT")
    private String respuestaTexto;

    @Column
    private Boolean correcta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEvaluacion", nullable = false) // ✅ CORREGIDO
    private EvaluacionEntity evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false) // ✅ CORREGIDO
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPregunta", nullable = false)
    private PreguntaEntity pregunta;
}
