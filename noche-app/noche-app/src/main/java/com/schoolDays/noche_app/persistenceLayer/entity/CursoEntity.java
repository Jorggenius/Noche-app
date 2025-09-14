package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa la tabla cursos
 */
@Entity
@Table(name = "Curso")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class CursoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob
    private String descripcion;

    private String nivel;
    private Integer duracionEstimada;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column(nullable = false)
    private Integer version = 1;

    @ManyToOne
    @JoinColumn(name = "creadoPor", nullable = false)
    private UsuarioEntity creadoPor;

    @OneToMany(mappedBy = "curso")
    private List<ModuloEntity> modulos;
}