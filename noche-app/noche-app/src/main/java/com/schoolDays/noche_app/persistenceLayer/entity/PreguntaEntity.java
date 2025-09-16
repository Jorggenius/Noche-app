package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidad unificada para todas las preguntas
 */
@Entity
@Table(name = "pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPregunta;

    @Lob
    @Column(nullable = false)
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPregunta tipoPregunta;

    @Column(nullable = false)
    private Integer orden = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_idevaluacion", nullable = false)
    private EvaluacionEntity evaluacion;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<RespuestaEntity> opciones;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<RespuestaUsuarioEntity> respuestasUsuario;

    public enum TipoPregunta {
        MULTIPLE_CHOICE, VERDADERO_FALSO, ABIERTA
    }
}
