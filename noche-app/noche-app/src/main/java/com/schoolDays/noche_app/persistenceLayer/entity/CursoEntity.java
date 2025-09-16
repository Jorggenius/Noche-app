package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa los cursos
 */
@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer duracion; // duración en horas

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;

    @Column(nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    @Column(nullable = false)
    private Integer version = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creadoPor", nullable = false)
    private UsuarioEntity creadoPor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<ModuloEntity> modulos;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<InscripcionEntity> inscripciones;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<CertificadoEntity> certificados;

    public enum Nivel {
        BASICO, INTERMEDIO, AVANZADO
    }
}
