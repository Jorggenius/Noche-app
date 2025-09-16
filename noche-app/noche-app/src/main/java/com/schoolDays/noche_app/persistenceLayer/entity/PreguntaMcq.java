package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaMcq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String respuestaCorrecta;

    @Column(nullable = false)
    private String respuestaIncorrecta1;

    @Column(nullable = false)
    private String getRespuestaIncorrecta2;

    @OneToOne
    @JoinColumn(name = "idevaluacion",nullable = false)
    private EvaluacionEntity evaluacion;
}
