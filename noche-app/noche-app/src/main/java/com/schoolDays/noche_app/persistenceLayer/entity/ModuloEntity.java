package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa los módulos de un curso
 */
@Entity
@Table(name = "modulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idModulo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoModulo tipo;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCurso", nullable = false)
    private CursoEntity curso;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    private List<EvaluacionEntity> evaluaciones;

    public enum TipoModulo {
        VIDEO, TEXTO, PDF, QUIZ, PRACTICA
    }
}
