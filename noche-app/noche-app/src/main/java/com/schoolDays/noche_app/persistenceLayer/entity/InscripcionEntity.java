package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa la tabla inscripciones
 */
@Entity
@Table(name = "Inscripcion")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class InscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInscripcion;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idCurso", nullable = false)
    private CursoEntity curso;

    @Column(nullable = false)
    private LocalDate fechaInscripcion;

    @Column(precision = 5, scale = 2)
    private BigDecimal progreso = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Inscrito;

    public enum Estado {
        Inscrito, En_progreso, Completado
    }
}