package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Evaluacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvaluacion tipo;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal puntajeMax; // CORREGIDO: era 'puntaje'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModulo", nullable = false)
    private ModuloEntity modulo;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PreguntaEntity> preguntas;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultadoEntity> resultados;

    public enum TipoEvaluacion {
        MCQ, ABIERTA, MIXTA
    }
}
