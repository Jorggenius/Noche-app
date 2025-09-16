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
public class RespuestaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int puntuacion;

    @Column(nullable = false)
    private LocalDate fecha;

    @OneToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private UsuarioEntity usuario;

    @OneToOne
    @JoinColumn(name = "idevaluacion", nullable = false)
    private EvaluacionEntity evaluacion;
}
