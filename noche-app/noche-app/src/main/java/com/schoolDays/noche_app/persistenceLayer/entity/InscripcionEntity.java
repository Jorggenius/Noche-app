package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa las inscripciones de usuarios a cursos
 */
@Entity
@Table(name = "Inscripcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionEntity extends BaseEntity { // ✅ AGREGAR extends BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInscripcion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal progreso = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.INSCRITO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false) // ✅ CORREGIDO
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCurso", nullable = false) // ✅ CORREGIDO
    private CursoEntity curso;

    public enum Estado {
        INSCRITO, EN_PROGRESO, COMPLETADO, SUSPENDIDO
    }
}
