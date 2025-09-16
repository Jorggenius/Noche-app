package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int progreso;

    @Column(nullable = false)
    private LocalDate fechaInscripcion;

    @Column(nullable = false)
    private boolean estado;

    @OneToOne
    @JoinColumn(name = "idusuario",nullable = false)
    private UsuarioEntity usuario;

    @OneToOne
    @JoinColumn(name = "idcurso", nullable = false)
    private CursoEntity curso;
}
