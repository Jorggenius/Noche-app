package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entidad que representa la tabla evaluaciones
 */
@Entity
@Table(name = "Evaluacion")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class EvaluacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvaluacion tipo;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal puntajeMax;

    @ManyToOne
    @JoinColumn(name = "idModulo", nullable = false)
    private ModuloEntity modulo;

    @OneToMany(mappedBy = "evaluacion")
    private List<PreguntaEntity> preguntas;

    public enum TipoEvaluacion {
        MCQ, Abierta, Mixta
    }
}
