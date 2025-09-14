package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa la tabla modulos
 */
@Entity
@Table(name = "Modulo")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class ModuloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idModulo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoModulo tipo;

    @Column(nullable = false)
    private Integer orden;

    private Integer version = 1;

    @ManyToOne
    @JoinColumn(name = "idCurso", nullable = false)
    private CursoEntity curso;

    @OneToMany(mappedBy = "modulo")
    private List<EvaluacionEntity> evaluaciones;

    public enum TipoModulo {
        Video, Texto, PDF, Quiz, Practica
    }
}